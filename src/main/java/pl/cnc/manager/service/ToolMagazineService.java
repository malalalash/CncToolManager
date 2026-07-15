package pl.cnc.manager.service;

import pl.cnc.manager.model.*;
import pl.cnc.manager.repository.ToolRepository;

import java.util.List;
import java.util.Scanner;

public class ToolMagazineService {

    private final ToolRepository repository;

    public ToolMagazineService(ToolRepository repository) {
        this.repository = repository;
    }

    public void addTool(Scanner scanner) {
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

            if (id.isEmpty()) {
                System.out.println("Error: Tool with ID '" + id +"' already exists!");
                return;
            }

            if (repository.existById(id)) {
                System.out.println("Tool with ID '"+ id + "' already exists!");
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

            repository.save(newTool);
            System.out.println("Tool added: \n" + newTool + "\n");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered. Tool not added.");
        }
    }

    public void removeTool(Scanner scanner) {
        System.out.println("Provide tool id to remove:");
        String id = scanner.nextLine().trim();
        boolean removed = repository.deleteById(id);
        if (removed) {
            System.out.println("Tool with id: " + id + " has been removed.");
        } else {
            System.out.println("No tool found with id: " + id);
        }
    }

    public void listTools() {
        List<Tool> tools = repository.findAll();
        if (tools.isEmpty()) {
            System.out.println("\nMagazine is empty.\n");
        } else {
            System.out.println("--- INVENTORY ---");
            for (Tool tool : tools) {
                System.out.println(tool + "\n");
            }
            System.out.println("------------------\n");
        }
    }
}
