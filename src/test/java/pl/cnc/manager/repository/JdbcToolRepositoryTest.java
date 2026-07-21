package pl.cnc.manager.repository;

import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import pl.cnc.manager.model.*;
import pl.cnc.manager.service.DatabaseConnectionService;

import java.io.IOException;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class JdbcToolRepositoryTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.4")
            .withDatabaseName("cnc_test")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("db/schema.sql");

    private static DatabaseConnectionService dbService;
    private JdbcToolRepository repository;

    @BeforeAll
    static void setUpDatabase() throws SQLException, IOException {
        dbService = new DatabaseConnectionService(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }

    @BeforeEach
    void cleanTable() throws SQLException {
        try (Connection conn = dbService.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE tools CASCADE");
        }
        repository = new JdbcToolRepository(dbService);
    }

    @Test
    @DisplayName("save() and findAll() should save and find Drill")
    void shouldSaveAndFindDrill() {
        Drill tool = new Drill("D1", "Drill 8mm", 8.00, 10);
        repository.save(tool);

        List<Tool> tools = repository.findAll();

        assertEquals(1, tools.size());
        Tool found = tools.getFirst();
        assertInstanceOf(Drill.class, found);
        assertEquals("D1", found.getId());
        assertEquals("Drill 8mm", found.getName());
        assertEquals(8.00, found.getDiameter());
        assertEquals(10, found.getQuantity());
    }

    @Test
    @DisplayName("save() and findAll() should save and find End Mill")
    void shouldSaveAndFindEndMill() {
        EndMill tool = new EndMill("EM1", "End Mill 8mm", 8.00, 3, 10);
        repository.save(tool);

        List<Tool> tools = repository.findAll();

        assertEquals(1, tools.size());
        EndMill found = (EndMill) tools.getFirst();
        assertInstanceOf(EndMill.class, found);
        assertEquals("EM1", found.getId());
        assertEquals("End Mill 8mm", found.getName());
        assertEquals(8.00, found.getDiameter());
        assertEquals(10, found.getQuantity());
        assertEquals(3, found.getFlutes());
    }

    @Test
    @DisplayName("save() and findAll() should save and find Face Mill")
    void shouldSaveAndFindFaceMill() {
        FaceMill tool = new FaceMill("FM1", "Face Mill 8mm", 8.00, 3, 10);
        repository.save(tool);

        List<Tool> tools = repository.findAll();

        assertEquals(1, tools.size());
        FaceMill found = (FaceMill) tools.getFirst();
        assertInstanceOf(FaceMill.class, found);
        assertEquals("FM1", found.getId());
        assertEquals("Face Mill 8mm", found.getName());
        assertEquals(8.00, found.getDiameter());
        assertEquals(10, found.getQuantity());
        assertEquals(3, found.getInserts());
    }

    @Test
    @DisplayName("save() and findAll() should save and find Tap")
    void shouldSaveAndFindTap() {
        Tap tool = new Tap("T1", "Tap M8", 8.00, 1.25, 10);
        repository.save(tool);

        List<Tool> tools = repository.findAll();

        assertEquals(1, tools.size());
        Tap found = (Tap) tools.getFirst();
        assertInstanceOf(Tap.class, found);
        assertEquals("T1", found.getId());
        assertEquals("Tap M8", found.getName());
        assertEquals(8.00, found.getDiameter());
        assertEquals(10, found.getQuantity());
        assertEquals(1.25, found.getPitch());
    }

    @Test
    @DisplayName("save() with duplicated ID violates PRIMARY KEY and throws ToolRepositoryException")
    void shouldFailOnDuplicatedId() {
        Drill tool = new Drill("D1", "Drill 8mm", 8.00, 10);
        repository.save(tool);

        ToolRepositoryException exception = assertThrows(ToolRepositoryException.class,
                () -> repository.save(tool));

        assertInstanceOf(SQLException.class, exception.getCause());
    }

    @Test
    @DisplayName("existsById() reflects database table")
    void shouldReportExistence() {
        assertFalse(repository.existsById("D1"));

        Drill tool = new Drill("D1", "Drill 8mm", 8.00, 10);
        repository.save(tool);
        assertTrue(repository.existsById("D1"));
    }

    @Test
    @DisplayName("deleteById() should delete existing tool")
    void shouldDeleteExistingTool() {
        Drill tool = new Drill("D1", "Drill 8mm", 8.00, 10);
        repository.save(tool);

        assertTrue(repository.deleteById("D1"));
        assertFalse(repository.existsById("D1"));
        assertFalse(repository.deleteById("D1"));
    }

    @Test
    @DisplayName("updateQuantity() should update tool's quantity")
    void shouldUpdateQuantity() {
        Drill tool = new Drill("D1", "Drill 8mm", 8.00, 10);

        repository.save(tool);
        assertTrue(repository.updateQuantity("D1", 1));

        Tool updatedTool = repository.findAll().getFirst();
        assertEquals(1, updatedTool.getQuantity());
    }

    @Test
    @DisplayName("updateQuantity() returns false for non existing id")
    void shouldReturnFalseForNonExistingId() {
        assertFalse(repository.updateQuantity("D1", 2));
    }

    @Test
    @DisplayName("database enforces CHECK (quantity >= 0) independently from java validation")
    void shouldEnforceNegativeQuantityAtDatabase() throws SQLException {
        String sql = "INSERT INTO tools (id, name, diameter, quantity, type, flutes, inserts, pitch) " +
                "VALUES (?, ?, ?, ? ,? ,? ,?, ?)";

        try (Connection conn = dbService.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "D1");
            pstmt.setString(2, "Drill");
            pstmt.setDouble(3, 1.0);
            pstmt.setInt(4, -9);
            pstmt.setString(5, "DRILL");

            assertThrows(SQLException.class, pstmt::executeUpdate);
        }
    }

    @Test
    @DisplayName("findAll() checks for empty database")
    void shouldReturnNothingIfDatabaseIsEmpty() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @DisplayName("findAll() should list multiple tools in database properly")
    void shouldListAllTools() {
        Tool drill = new Drill("D1", "Drill", 2.00, 1);
        Tool endMill = new EndMill("EM1", "End Mill", 6.00, 3, 4);
        Tool tap = new Tap("T1", "Tap", 8.00, 1.25, 4);

        repository.save(drill);
        repository.save(endMill);
        repository.save(tap);

        List<Tool> tools = repository.findAll();

        assertEquals(3, repository.findAll().size());
        assertInstanceOf(Drill.class, tools.getFirst());
        assertInstanceOf(EndMill.class, tools.get(1));
        assertInstanceOf(Tap.class, tools.get(2));
    }

    @Nested
    @DisplayName("Tests for issueTool")
    class IssueToolTests {
        @Test
        @DisplayName("issueTool() test should issue correct amount")
        void shouldIssueCorrectAmount() {
            Tool drill = new Drill("D1", "Drill", 2.00, 5);
            repository.save(drill);
            assertTrue(repository.issueTool("D1", 1));
            Tool updatedTool = repository.findAll().getFirst();
            assertEquals(4, updatedTool.getQuantity());
        }

        @Test
        @DisplayName("issueTool() should return false when tool does not exist")
        void shouldReturnFalseWhenToolDoesNotExist() {
            assertFalse(repository.issueTool("D1", 6));
        }

        @Test
        @DisplayName("issueTool() should return false when amount exceeds available quantity")
        void shouldReturnFalseWhenAmountExceedsQuantity() {
            Tool drill = new Drill("D1", "Drill", 2.00, 5);
            repository.save(drill);

            assertFalse(repository.issueTool("D1", 6));
        }

        @Test
        @DisplayName("issueTool() should not change quantity when amount exceeds available quantity")
        void shouldNotChangeQuantityOnFailedIssue() {
            Tool drill = new Drill("D1", "Drill", 2.00, 5);
            repository.save(drill);

            assertFalse(repository.issueTool("D1", 6));

            Tool unchanged = repository.findAll().getFirst();
            assertEquals(5, unchanged.getQuantity());
        }

        @Test
        @DisplayName("issueTool() should create a log entry in tool_issues")
        void shouldCreateIssueLogEntry() throws SQLException {
            Tool drill = new Drill("D1", "Drill", 2.00, 5);
            repository.save(drill);

            repository.issueTool("D1", 2);

            try (Connection conn = dbService.connect();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT amount, issued_at FROM tool_issues WHERE tool_id = ?")) {
                stmt.setString(1, "D1");
                try (ResultSet rs = stmt.executeQuery()) {
                    assertTrue(rs.next());
                    assertEquals(2, rs.getInt("amount"));
                    assertNotNull(rs.getTimestamp("issued_at"));
                    assertFalse(rs.next());
                }
            }
        }

        @Test
        @DisplayName("issueTool() should throw IllegalArgumentException for non-positive amount")
        void shouldThrowExceptionForNonPositiveAmount() {
            assertThrows(IllegalArgumentException.class, () -> repository.issueTool("D1", 0));
            assertThrows(IllegalArgumentException.class, () -> repository.issueTool("D1", -3));
        }

        @Test
        @DisplayName("issueTool() should allow issuing exact available amount")
        void shouldAllowIssuingExactAmount() {
            Tool drill = new Drill("D1", "Drill", 10.00, 5);
            repository.save(drill);

            assertTrue(repository.issueTool("D1", 5));

            Tool updatedDrill = repository.findAll().getFirst();
            assertEquals(0, updatedDrill.getQuantity());
        }

        @Test
        @DisplayName("checks CONSTRAINT CHECK (amount > 0) at the database level")
        void shouldEnforceNonPositiveAmountAtDatabase() throws SQLException {
            Tool drill = new Drill("D1", "Drill", 10.00, 5);
            repository.save(drill);

            String sql = "INSERT INTO tool_issues (tool_id, amount) VALUES (?, ?)";
            try (Connection conn = dbService.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "D1");
                pstmt.setInt(2, -3);

                assertThrows(SQLException.class, pstmt::executeUpdate);
            }
        }
    }
}