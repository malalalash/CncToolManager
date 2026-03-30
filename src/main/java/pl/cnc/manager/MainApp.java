package pl.cnc.manager;

import pl.cnc.manager.model.Drill;
import pl.cnc.manager.model.Tool;
import pl.cnc.manager.model.ToolType;
import pl.cnc.manager.service.FileService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void startupMessage() {
        System.out.println("\nAvailable options:");
        System.out.println("'1' to add new tool");
        System.out.println("'2' to delete tool");
        System.out.println("'3' to view all tools in magazine");
        System.out.println("'0' to exit\n");
    }
    public static void main(String[] args) {
        System.out.println("Welcome to CNC Tool Manager");
        List<Tool> magazine = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            startupMessage();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.println("Select tool type:");
                    ToolType[] types = ToolType.values();
                    for (int i = 0; i < types.length; i++) {
                        System.out.println((i + 1) + " - " + types[i]);
                    }

                    try {
                        int typeIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;

                        if (typeIndex < 0 || typeIndex >= types.length) {
                            System.out.println("Invalid tool type selection.");
                            break;
                        }

                        ToolType selectedType = types[typeIndex];

                        System.out.println("Provide id:");
                        String id = scanner.nextLine().trim();
                        System.out.println("Provide name:");
                        String name = scanner.nextLine().trim();
                        System.out.println("Provide diameter:");
                        double diameter = Double.parseDouble(scanner.nextLine().trim());
                        System.out.println("Provide quantity:");
                        int quantity = Integer.parseInt(scanner.nextLine().trim());

                        Tool newTool = new Drill(id, name, diameter, quantity);
                        magazine.add(newTool);
                        System.out.println("Tool added: " + newTool + "\n");

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number entered. Tool not added.");
                    }
                    break;
                case "2":
                    if (magazine.isEmpty()) {
                        System.out.println("Magazine is empty, nothing to remove.");
                        break;
                    }
                    System.out.println("Provide tool id to remove:");
                    String id = scanner.nextLine().trim();
                    boolean removed = magazine.removeIf(tool -> tool.getId().equals(id));
                    if (removed) {
                        System.out.println("Tool with id: " + id + " has been removed.");
                    } else {
                        System.out.println("No tool found with id: " + id);
                    }
                    break;
                case "3":
                    if (magazine.isEmpty()) {
                        System.out.println("\nMagazine is empty.\n");
                    } else {
                        for (Tool tool : magazine) {
                            System.out.println(tool + "\n");
                        }
                    }
                    break;
                case "0": {
                    running = false;
                    break;
                }
                default:
                    System.out.println("non");
            }
        }
        scanner.close();
    }
}


