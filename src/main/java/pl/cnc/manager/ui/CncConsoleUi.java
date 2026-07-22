package pl.cnc.manager.ui;

import pl.cnc.manager.model.*;
import pl.cnc.manager.repository.ToolRepositoryException;
import pl.cnc.manager.service.ToolMagazineService;

import java.util.List;
import java.util.Scanner;

import static pl.cnc.manager.model.OperationType.PICKUP;
import static pl.cnc.manager.model.OperationType.RETURN;

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
                case "5" -> handleIssueReturnTool();
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
        System.out.println("'5' to issue/return tool");
        System.out.println("'0' to exit\n");
    }

    private void handleAddTool() {
        runSafely("Add tool", () -> {
            System.out.println("Select tool type:");
            ToolType[] types = ToolType.values();
            for (int i = 0; i < types.length; i++) {
                System.out.println((i + 1) + " - " + types[i]);
            }

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
        });
    }

    private void handleRemoveTool() {
        runSafely("Remove tool", () -> {
            System.out.println("Provide tool id to remove:");
            String id = scanner.nextLine().trim();

            boolean removed = toolService.removeTool(id);
            if (removed) {
                System.out.println("Tool with id: " + id + " has been successfully removed.");
            } else {
                System.out.println("No tool found with id: " + id);
            }
        });
    }

    private void handleListTools() {
        runSafely("List tool(s)", () -> {
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
        });
    }

    private void handleUpdateQuantity() {
        runSafely("Update tool", () -> {
            System.out.println("Provide tool id to update:");
            String id = scanner.nextLine().trim();

            int quantity = readIntInput("Provide new quantity:");

            boolean updated = toolService.updateQuantity(id, quantity);
            if (updated) {
                System.out.println("Tool with id: " + id + " now has quantity of: " + quantity);
            } else {
                System.out.println("No tool with id: " + id);
            }
        });
    }

    private void handleIssueReturnTool() {
        runSafely("issue/return tool", () -> {

            System.out.println("SELECT OPTION:\n1: Issue Tool\n2: Return tool\n0: Exit");
            OperationType type = null;
            while (type == null) {
                try {
                    int selectedOption = Integer.parseInt(scanner.nextLine().trim());
                    switch (selectedOption) {
                        case 1 -> {
                            type = PICKUP;
                        }
                        case 2 -> {
                            type = RETURN;
                        }
                        case 0 -> {
                            return;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please select '1' or '2' to issue/return tool or '0' to exit");
                }
            }
            String operation = type == PICKUP ? "issue" : "return";

            System.out.printf("Provide tool id to %s: ", operation);
            String id = scanner.nextLine().trim();

            System.out.printf("\nProvide quantity to %s: ", operation);
            int amount = readIntInput(String.format("Provide quantity to %s: ", operation));

            boolean success = toolService.issueReturnTool(id, amount, type);
            if (success) {
                System.out.printf("Successfully %s tool!\n", (type == PICKUP ? "issued" : "returned"));
            } else {
                System.out.println("Failed! No tool with id: " + id);
            }
        });
    }

    private void runSafely(String actionLabel, Runnable action) {
        try {
            action.run();
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered. " + actionLabel + " aborted.");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        } catch (ToolRepositoryException e) {
            System.err.println("Database error, Try again!");
        }
    }

    private int readIntInput(String input) {
        while (true) {
            System.out.println(input);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please provide valid integer!");
            }
        }
    }
}