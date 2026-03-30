package pl.cnc.manager;

import pl.cnc.manager.model.Drill;
import pl.cnc.manager.model.Tool;
import pl.cnc.manager.service.FileService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void startupMessage() {
        System.out.println("Welcome to CNC Tool Manager");
        System.out.println("Available options:");
        System.out.println("'1' to create a new tool");
        System.out.println("'2' to delete tool");
        System.out.println("'3' to view all tools");
        System.out.println("'0' to exit");
    }
    static void main() {
        startupMessage();
        List<Tool> magazine = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String input = scanner.next();

            switch (input) {
                case "1":
                    System.out.println("1");
                    break;
                case "2":
                    System.out.println("2");
                    break;
                case "3":
                    System.out.println("3");
                    break;
                case "0": {
                    running = false;
                    break;
                }
                default:
                    System.out.println("Brak");
            }
        }
        scanner.close();
    }
}


