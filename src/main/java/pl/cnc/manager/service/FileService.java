package pl.cnc.manager.service;

import pl.cnc.manager.model.Tool;

import java.io.*;
import java.util.Arrays;
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

    public void loadFromFile() {
        File file = new File(FILE_NAME);
        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String line = input.nextLine();
                if (line.isBlank()) {
                    continue;
                }

                String[] data = line.split(",");
                System.out.println(data[0]);
                if (data.length < 4) {
                    continue;
                }
                String id = data[0];
                String name = data[1];
                double diameter = Double.parseDouble(data[2]);
                int quantity = Integer.parseInt(data[3]);

                //System.out.printf("ID: ,%s NAME: ,%s DIAMETER: ,%.2f QUANTITY: ,%d ", id, name, diameter, quantity);
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
