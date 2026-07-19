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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
        Drill tool =new Drill("D1", "Drill 8mm", 8.00, 10);
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
}