package pl.cnc.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.cnc.manager.repository.JdbcToolIssueRepository;
import pl.cnc.manager.repository.JdbcToolRepository;
import pl.cnc.manager.repository.ToolIssueRepository;
import pl.cnc.manager.repository.ToolRepository;
import pl.cnc.manager.service.DatabaseConnectionService;
import pl.cnc.manager.service.ToolIssueService;
import pl.cnc.manager.service.ToolMagazineService;
import pl.cnc.manager.ui.CncConsoleUi;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MainApp {
    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        System.out.println("Welcome to CNC Tool Manager");
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (isBlank(url) || isBlank(user) || isBlank(password)) {
            log.warn("Startup aborted: DB_URL, DB_USER, DB_PASSWORD env variable is missing.");

            System.out.println("Database configuration is missing." +
                    " Set DB_URL, DB_USER, DB_PASSWORD environment variables.");
            return;
        }

        DatabaseConnectionService dbService = new DatabaseConnectionService(url, user, password);
        try {
            try (Connection conn = dbService.connect()) {
                if (conn != null && !conn.isClosed()) {
                    log.info("Application started, dadabase connection acquired.");
                    System.out.println("Connected to database!");
                }
            } catch (SQLException e) {
                log.error("Cannot connect to database at startup.", e);
                System.err.println("Cannot connect to database!");
                return;
            }

            ToolRepository repository = new JdbcToolRepository(dbService);
            ToolMagazineService toolService = new ToolMagazineService(repository);

            ToolIssueRepository toolIssueRepository = new JdbcToolIssueRepository(dbService);
            ToolIssueService toolIssueService = new ToolIssueService(toolIssueRepository);

            try (Scanner scanner = new Scanner(System.in)) {
                CncConsoleUi ui = new CncConsoleUi(toolService, toolIssueService, scanner);
                ui.start();
            }
        } finally {
            dbService.close();
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

