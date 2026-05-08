package pl.cnc.manager.service;

import pl.cnc.manager.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileService {

    private static final String FILE_NAME = "inventory.csv";

    public void saveToFile(List<Tool> tools) {
        if (tools == null || tools.isEmpty()) {
            System.out.println("No tools found");
            return;
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) {
            for (Tool tool : tools) {
                writer.println(tool.toCsv());
            }
            System.out.println("Saved " + tools.size() + " tool(s) to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to save tools to file: " + FILE_NAME, e);
        }
    }

    public List<Tool> loadFromFile() {
        List<Tool> magazine = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) return magazine;

        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String line = input.nextLine();
                if (line.isBlank()) continue;

                String[] data = line.split(",");
                if (data.length < 5) continue;

                try {
                    ToolType type = ToolType.valueOf(data[0].trim().toUpperCase());
                    String id = data[1];
                    String name = data[2];
                    double diameter = Double.parseDouble(data[3]);
                    int quantity = Integer.parseInt(data[data.length - 1]);

                    Tool tool = switch (type) {
                        case DRILL -> new Drill(id, name, diameter, quantity);
                        case END_MILL -> {
                            if (data.length < 6) {
                                System.err.println("Wrong END_MILL line: " + line);
                                yield null;
                            }
                            int flutes = Integer.parseInt(data[4]);
                            yield new EndMill(id, name, diameter, flutes, quantity);
                        }
                        case FACE_MILL -> {
                            if (data.length < 6) {
                                System.err.println("Wrong FACE_MILL line: " + line);
                                yield null;
                            }
                            int inserts = Integer.parseInt(data[4]);
                            yield new FaceMill(id, name, diameter, inserts, quantity);
                        }
                        default -> {
                            System.err.println("Unsupported type: " + type);
                            yield null;
                        }
                    };
                    if (tool != null) magazine.add(tool);
                } catch (IllegalArgumentException e) {
                    System.err.println("Unknown tool type: " + data[0]);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return magazine;
    }
}
