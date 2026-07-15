package pl.cnc.manager;

import pl.cnc.manager.model.*;
import pl.cnc.manager.service.DatabaseService;
import pl.cnc.manager.service.ToolMagazine;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Welcome to CNC Tool Manager");
        DatabaseService db = new DatabaseService();

        try (Connection connection = db.connect()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connected successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Couldn't connect to database");
            e.printStackTrace();
        }



        ToolMagazine tm = new ToolMagazine();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            startupMessage();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> System.out.println("1");
                case "2" -> System.out.println("2");
                case "3" -> System.out.println("3");
                case "0" -> {
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


