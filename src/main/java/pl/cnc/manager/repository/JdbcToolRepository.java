package pl.cnc.manager.repository;

import pl.cnc.manager.model.*;
import pl.cnc.manager.service.DatabaseConnectionService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcToolRepository implements ToolRepository {
    private final DatabaseConnectionService dbService;

    public JdbcToolRepository(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    @Override
    public List<Tool> findAll() {
        List<Tool> tools = new ArrayList<>();
        String sql = "SELECT id, name, diameter, quantity, type, flutes, inserts, pitch FROM tools";

        try (Connection conn = dbService.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                double diameter = rs.getDouble("diameter");
                int quantity = rs.getInt("quantity");
                ToolType type = ToolType.valueOf(rs.getString("type"));

                Tool tool = switch (type) {
                    case DRILL -> new Drill(id, name, diameter, quantity);
                    case END_MILL -> new EndMill(id, name, diameter, rs.getInt("flutes"), quantity);
                    case FACE_MILL -> new FaceMill(id, name, diameter, rs.getInt("inserts"), quantity);
                    case TAP -> new Tap(id, name, diameter, rs.getDouble("pitch"), quantity);
                };
                tools.add(tool);
            }
        } catch (SQLException e) {
            throw new ToolRepositoryException("Failed to fetch tools: ", e);
        }
        return tools;
    }

    @Override
    public boolean existsById(String id) {
        String sql = "SELECT 1 FROM tools WHERE id = ?";
        try (Connection conn = dbService.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new ToolRepositoryException("Error checking for tool: ", e);
        }
    }

    @Override
    public void save(Tool tool) {
        String sql = "INSERT INTO tools (id, name, diameter, quantity, type, flutes, inserts, pitch) " +
                "VALUES (?, ?, ?, ? ,? ,? ,?, ?)";

        try (Connection conn = dbService.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {

            pstmt.setString(1, tool.getId());
            pstmt.setString(2, tool.getName());
            pstmt.setDouble(3, tool.getDiameter());
            pstmt.setInt(4, tool.getQuantity());

            switch (tool) {
                case Drill drill -> {
                    pstmt.setString(5, ToolType.DRILL.name());
                    pstmt.setNull(6, Types.INTEGER);
                    pstmt.setNull(7, Types.INTEGER);
                    pstmt.setNull(8, Types.DOUBLE);
                }
                case EndMill endMill -> {
                    pstmt.setString(5, ToolType.END_MILL.name());
                    pstmt.setInt(6, endMill.getFlutes());
                    pstmt.setNull(7, Types.INTEGER);
                    pstmt.setNull(8, Types.DOUBLE);
                }
                case FaceMill faceMill -> {
                    pstmt.setString(5, ToolType.FACE_MILL.name());
                    pstmt.setNull(6, Types.INTEGER);
                    pstmt.setInt(7, faceMill.getInserts());
                    pstmt.setNull(8, Types.DOUBLE);
                }
                case Tap tap -> {
                    pstmt.setString(5, ToolType.TAP.name());
                    pstmt.setNull(6, Types.INTEGER);
                    pstmt.setNull(7, Types.INTEGER);
                    pstmt.setDouble(8, tap.getPitch());
                }
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ToolRepositoryException("Cannot save tool: ", e);
        }
    }

    @Override
    public boolean deleteById(String id) {
        String sql = "DELETE FROM tools WHERE id = ?";
        try (Connection conn = dbService.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ToolRepositoryException("Cannot delete tool: ", e);
        }
    }

    @Override
    public boolean updateQuantity(String id, int quantity) {
        String sql = "UPDATE tools SET quantity = ? WHERE id = ?";
        try (Connection conn = dbService.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantity);
            pstmt.setString(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ToolRepositoryException("Cannot update quantity for tool (id=" + id + "): ", e);
        }
    }
    @Override
    public boolean issueTool(String id, int amount) {
        return changeQuantity(id, amount, OperationType.PICKUP);
    }

    @Override
    public boolean returnTool(String id, int amount) {
        return changeQuantity(id, amount, OperationType.RETURN);
    }

    private boolean changeQuantity(String id, int amount, OperationType operationType) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Tool ID cannot be empty.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        int delta = operationType == OperationType.PICKUP ? -amount : amount;

        String selectSQL = "SELECT quantity FROM tools WHERE id = ? FOR UPDATE";
        String updateSQL = "UPDATE tools SET quantity = quantity + ? WHERE id = ?";
        String insertSQL = "INSERT INTO tool_issues (tool_id, amount, operation_type) VALUES (?, ?, ?)";

        try (Connection conn = dbService.connect()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement select = conn.prepareStatement(selectSQL)) {
                    select.setString(1, id);
                    try (ResultSet rs = select.executeQuery()) {
                        if (!rs.next() || rs.getInt("quantity") + delta < 0) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement update = conn.prepareStatement(updateSQL)) {
                    update.setInt(1, delta);
                    update.setString(2, id);
                    update.executeUpdate();
                }

                try (PreparedStatement insert = conn.prepareStatement(insertSQL)) {
                    insert.setString(1, id);
                    insert.setInt(2, amount);
                    insert.setString(3, operationType.name());
                    insert.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new ToolRepositoryException(
                    "Failed to " + operationType.name().toLowerCase() + " tool (id=" + id + "): ", e);
        }
    }
}
