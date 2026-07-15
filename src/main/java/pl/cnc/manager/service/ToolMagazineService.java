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

    public void addTool(Tool tool) {
        if (tool == null) {
            throw new IllegalArgumentException("Tool details cannot be null.");
        }
        if (tool.getId() == null || tool.getId().isBlank()) {
            throw new IllegalArgumentException("Tool ID cannot be empty.");
        }
        if (repository.existById(tool.getId())) {
            throw new IllegalArgumentException("Tool with ID '" + tool.getId() + "' already exists!");
        }

        repository.save(tool);
    }

    public boolean removeTool(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Tool ID cannot be empty.");
        }
        return repository.deleteById(id);
    }

    public List<Tool> getAllTools() {
        return repository.findAll();
    }
}
