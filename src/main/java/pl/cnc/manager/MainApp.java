package pl.cnc.manager;

import pl.cnc.manager.model.Drill;
import pl.cnc.manager.model.EndMill;
import pl.cnc.manager.model.Tool;
import pl.cnc.manager.model.ToolType;
import pl.cnc.manager.service.FileService;

import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Welcome to CNC Tool Manager");
        FileService fs = new FileService();
        List<Tool> magazine = fs.loadFromFile();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            startupMessage();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> addTool(magazine, scanner);
                case "2" -> removeTool(magazine, scanner);
                case "3" -> listTools(magazine);
                case "0" -> {
                    fs.saveToFile(magazine);
                    running = false;
                }
                default -> System.out.println("Uknown option, try again");
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

    private static void addTool(List<Tool> magazine, Scanner scanner) {
        System.out.println("Select tool type:");
        ToolType[] types = ToolType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + " - " + types[i]);
        }

        try {
            int typeIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;

            if (typeIndex < 0 || typeIndex >= types.length) {
                System.out.println("Invalid tool type selection.");
                return;
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

            Tool newTool = switch (selectedType) {
                case DRILL -> new Drill(id, name, diameter, quantity);
                case END_MILL -> {
                    System.out.println("Provide number of flutes:");
                    int flutes = Integer.parseInt(scanner.nextLine().trim());
                    yield new EndMill(id, name, diameter, flutes, quantity);
                }
                case FACE_MILL -> null;
                case THREAD_MILL -> null;
            };
            if (newTool != null) {
                magazine.add(newTool);
                System.out.println("Tool added: \n" + newTool + "\n");
            } else {
                System.out.println("Could not add tool.");
            }
            System.out.println("Tool added: \n" + newTool + "\n");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered. Tool not added.");
        }
    }

    private static void removeTool(List<Tool> magazine, Scanner scanner) {
        if (magazine.isEmpty()) {
            System.out.println("Magazine is empty, nothing to remove.");
            return;
        }

        System.out.println("Provide tool id to remove:");
        String id = scanner.nextLine().trim();
        boolean removed = magazine.removeIf(tool -> tool.getId().equals(id));
        if (removed) {
            System.out.println("Tool with id: " + id + " has been removed.");
        } else {
            System.out.println("No tool found with id: " + id);
        }
    }

    private static void listTools(List<Tool> magazine) {
        if (magazine.isEmpty()) {
            System.out.println("\nMagazine is empty.\n");
        } else {
            for (Tool tool : magazine) {
                System.out.println(tool + "\n");
            }
        }
    }
}


