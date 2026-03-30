package pl.cnc.manager.service;

import pl.cnc.manager.model.Tool;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
}
