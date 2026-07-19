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
    public boolean existById(String id) {
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
                default -> throw new IllegalArgumentException("Unexpected value: " + tool);
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
}
