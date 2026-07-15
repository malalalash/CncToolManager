package pl.cnc.manager;

import pl.cnc.manager.repository.JdbcToolRepository;
import pl.cnc.manager.repository.ToolRepository;
import pl.cnc.manager.service.DatabaseConnectionService;
import pl.cnc.manager.service.ToolMagazineService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Welcome to CNC Tool Manager");
        DatabaseConnectionService dbService = new DatabaseConnectionService();

        try (Connection conn = dbService.connect()) {
            if(conn != null && !conn.isClosed()) {
                System.out.println("Connected to database!");
            }
        } catch (SQLException e) {
            System.err.println("Cannot connect to database: " + e.getMessage());
            return;
        }

        ToolRepository repository = new JdbcToolRepository(dbService);
        ToolMagazineService toolService = new ToolMagazineService(repository);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            startupMessage();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> toolService.addTool(scanner);
                case "2" -> toolService.removeTool(scanner);
                case "3" -> toolService.listTools();
                case "0" -> {
                    System.out.println("GOODBYE!");
                    running = false;
                }
                default -> System.out.println("Unknown option, try again");
            }
        }
        scanner.close();
    }

    private static void startupMessage() {
        System.out.println("\nAvailable options:");
        System.out.println("'1' to add new tool");
        System.out.println("'2' to delete tool");
        System.out.println("'3' to view all tools in magazine");
        System.out.println("'0' to exit\n");
    }
}


