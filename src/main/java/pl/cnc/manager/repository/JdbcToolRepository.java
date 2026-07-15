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
        String sql = "SELECT * FROM tools";

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
            System.err.println("Cannot fetch tools: " + e.getMessage());
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
            System.err.println("Error checking for tool: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void save(Tool tool) {

    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }
}
