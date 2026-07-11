package pl.cnc.manager.service;

import pl.cnc.manager.model.*;

import java.util.List;
import java.util.Scanner;

public class ToolMagazine {
    public void addTool(List<Tool> magazine, Scanner scanner) {
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

            boolean idExists = magazine.stream().anyMatch(tool -> tool.getId().equalsIgnoreCase(id));
            if (idExists) {
                System.out.println("Error: Tool with ID '" + id +"' already exists!");
                return;
            }

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
                case FACE_MILL -> {
                    System.out.println("Provide number of inserts:");
                    int inserts = Integer.parseInt(scanner.nextLine().trim());
                    yield new FaceMill(id, name, diameter, inserts, quantity);
                }
                case TAP -> {
                    System.out.println("Provide pitch of a TAP:");
                    double pitch = Double.parseDouble(scanner.nextLine().trim());
                    yield new Tap(id, name, diameter, pitch, quantity);
                }
            };
            if (newTool != null) {
                magazine.add(newTool);
                System.out.println("Tool added: \n" + newTool + "\n");
            } else {
                System.out.println("Could not add tool.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered. Tool not added.");
        }
    }

    public void removeTool(List<Tool> magazine, Scanner scanner) {
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

    public void listTools(List<Tool> magazine) {
        if (magazine.isEmpty()) {
            System.out.println("\nMagazine is empty.\n");
        } else {
            for (Tool tool : magazine) {
                System.out.println(tool + "\n");
            }
        }
    }
}
