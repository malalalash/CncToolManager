package pl.cnc.manager.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import pl.cnc.manager.model.*;
import pl.cnc.manager.service.DatabaseConnectionService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class JdbcToolIssueRepositoryTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.4")
            .withDatabaseName("cnc_test")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("db/schema.sql");

    private static DatabaseConnectionService dbService;
    private JdbcToolRepository toolRepository;
    private JdbcToolIssueRepository issueRepository;

    @BeforeAll
    static void setUpDatabase() throws SQLException, IOException {
        dbService = new DatabaseConnectionService(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }

    @BeforeEach
    void cleanTable() throws SQLException {
        try (Connection conn = dbService.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE tool_issues CASCADE");
        }
        issueRepository = new JdbcToolIssueRepository(dbService);
        toolRepository = new JdbcToolRepository(dbService);
    }

    @Test
    @DisplayName("findIssueHistory() should return empty list when no issues exist")
    void shouldReturnEmptyHistory() {
        assertTrue(issueRepository.findIssueHistory().isEmpty());
    }

    @Test
    @DisplayName("findIssueHistory() should return tool data after issue")
    void shouldReturnHistoryWithToolData() {
        Tool drill = new Drill("D1", "Drill 8mm", 8.00, 5);
        toolRepository.save(drill);
        toolRepository.issueTool("D1", 2);

        List<ToolIssue> history = issueRepository.findIssueHistory();

        assertEquals(1, history.size());
        ToolIssue issue = history.getFirst();
        assertEquals("D1", issue.toolId());
        assertEquals("Drill 8mm", issue.toolName());
        assertEquals(ToolType.DRILL, issue.toolType());
        assertEquals(2, issue.amount());
        assertEquals(OperationType.PICKUP, issue.operationType());
        assertNotNull(issue.issuedAt());
    }

    @Test
    @DisplayName("findIssueHistory() should order by issued_at descending")
    void shouldOrderByIssuedAtDesc () throws InterruptedException {
        Tool drill = new Drill("D2", "Drill 8mm", 8.00, 5);
        toolRepository.save(drill);
        toolRepository.issueTool("D2", 1);
        Thread.sleep(10);
        toolRepository.returnTool("D2",1);

        List<ToolIssue> history = issueRepository.findIssueHistory();

        assertEquals(2, history.size());
        assertEquals(OperationType.RETURN, history.getFirst().operationType());
        assertEquals(OperationType.PICKUP, history.get(1).operationType());
    }
}