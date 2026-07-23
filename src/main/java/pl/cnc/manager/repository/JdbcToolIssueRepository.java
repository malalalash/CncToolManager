package pl.cnc.manager.repository;

import pl.cnc.manager.model.OperationType;
import pl.cnc.manager.model.ToolIssue;
import pl.cnc.manager.model.ToolType;
import pl.cnc.manager.service.DatabaseConnectionService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcToolIssueRepository implements ToolIssueRepository{
    private final DatabaseConnectionService dbService;

    public JdbcToolIssueRepository(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    @Override
    public List<ToolIssue> findIssueHistory() {
        List<ToolIssue> history = new ArrayList<>();
        String sql = """
        SELECT ti.id, ti.tool_id, t.name, t.type, ti.amount, ti.operation_type, ti.issued_at
        FROM tool_issues ti
        JOIN tools t ON ti.tool_id = t.id
        ORDER BY ti.issued_at DESC
        """;
        try (Connection conn = dbService.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String toolId = rs.getString("tool_id");
                String name = rs.getString("name");
                ToolType toolType = ToolType.valueOf(rs.getString("type"));
                int amount = rs.getInt("amount");
                OperationType operationType = OperationType.valueOf(rs.getString("operation_type"));
                LocalDateTime issuedAt = rs.getTimestamp("issued_at").toLocalDateTime();

                history.add(new ToolIssue(id, toolId, name, toolType, amount, operationType, issuedAt));
            }

        } catch (SQLException e) {
            throw new ToolRepositoryException("Failed to get values: ", e);
        }
        return history;
    }
}
