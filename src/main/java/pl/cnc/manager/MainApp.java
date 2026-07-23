package pl.cnc.manager;

import pl.cnc.manager.repository.JdbcToolRepository;
import pl.cnc.manager.repository.ToolRepository;
import pl.cnc.manager.service.DatabaseConnectionService;
import pl.cnc.manager.service.ToolMagazineService;
import pl.cnc.manager.ui.CncConsoleUi;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Welcome to CNC Tool Manager");
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (url.isBlank() || user.isBlank() || password.isBlank()) {
            System.out.println("Database configuration is missing.");
            return;
        }

        DatabaseConnectionService dbService = new DatabaseConnectionService(url, user, password);

        try (Connection conn = dbService.connect()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Connected to database!");
            }
        } catch (SQLException e) {
            System.err.println("Cannot connect to database!");
            return;
        }

        ToolRepository repository = new JdbcToolRepository(dbService);
        ToolMagazineService toolService = new ToolMagazineService(repository);

        try (Scanner scanner = new Scanner(System.in)) {
            CncConsoleUi ui = new CncConsoleUi(toolService, scanner);
            ui.start();
        } finally {
            dbService.close();
        }
    }
}

