package pl.cnc.manager.ui;

import pl.cnc.manager.model.*;
import pl.cnc.manager.repository.ToolRepositoryException;
import pl.cnc.manager.service.ToolMagazineService;

import java.util.List;
import java.util.Scanner;

public class CncConsoleUi {

    private final ToolMagazineService toolService;
    private final Scanner scanner;

    public CncConsoleUi(ToolMagazineService toolService, Scanner scanner) {
        this.toolService = toolService;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> handleAddTool();
                case "2" -> handleRemoveTool();
                case "3" -> handleListTools();
                case "4" -> handleUpdateQuantity();
                case "0" -> {
                    System.out.println("GOODBYE!");
                    running = false;
                }
                default -> System.out.println("Unknown option, try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nAvailable options:");
        System.out.println("'1' to add new tool");
        System.out.println("'2' to delete tool");
        System.out.println("'3' to view all tools in magazine");
        System.out.println("'4' to update quantity");
        System.out.println("'5' to issue tool");
        System.out.println("'0' to exit\n");
    }

    private void handleAddTool() {
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

            toolService.addTool(newTool);
            System.out.println("Tool successfully added: \n" + newTool + "\n");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered. Tool was not added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (ToolRepositoryException e) {
            System.err.println("Database error: Could not add tool: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected system error occurred: " + e.getMessage());
        }
    }

    private void handleRemoveTool() {
        System.out.println("Provide tool id to remove:");
        String id = scanner.nextLine().trim();

        try {
            boolean removed = toolService.removeTool(id);
            if (removed) {
                System.out.println("Tool with id: " + id + " has been successfully removed.");
            } else {
                System.out.println("No tool found with id: " + id);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ToolRepositoryException e) {
            System.err.println("Database error: Could not remove tool: " + e.getMessage());
        }
    }

    private void handleListTools() {
        try {
            List<Tool> tools = toolService.getAllTools();
            if (tools.isEmpty()) {
                System.out.println("\nMagazine is empty.\n");
            } else {
                System.out.println("--- INVENTORY ---");
                for (Tool tool : tools) {
                    System.out.println(tool + "\n");
                }
                System.out.println("------------------\n");
            }
        } catch (ToolRepositoryException e) {
            System.out.println("Error reading inventory: " + e.getMessage());
        }
    }

    private void handleUpdateQuantity() {
        System.out.println("Provide tool id to update:");
        String id = scanner.nextLine().trim();

        System.out.println("Provide new quantity:");
        try {
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            boolean updated = toolService.updateQuantity(id, quantity);
            if (updated) {
                System.out.println("Tool with id: " + id + " now has quantity of: " + quantity);
            } else {
                System.out.println("No tool with id: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong quantity provided");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (ToolRepositoryException e) {
            System.out.println("Database error: Could not update quantity: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected system error occurred: " + e.getMessage());
        }
    }
    private void handleIssueTool() {
        System.out.println("Provide tool id to issue:");
        String id = scanner.nextLine().trim();

        System.out.println("Provide quantity to issue:");
        try {
            int amount = Integer.parseInt(scanner.nextLine().trim());
            boolean issued = toolService.issueTool(id, amount);
            if (issued) {
                System.out.println("Tool with id: " + id + " has been issued with amount of: " + amount);
            } else {
                System.out.println("No tool with id: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong amount provided");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        } catch (ToolRepositoryException e) {
            System.out.println("Database error: Could not issue tool: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected system error occurred: " + e.getMessage());
        }
    }
}