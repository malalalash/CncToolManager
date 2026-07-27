package pl.cnc.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.cnc.manager.model.*;
import pl.cnc.manager.repository.ToolRepository;

import java.util.List;

public class ToolMagazineService {
    private static final Logger log = LoggerFactory.getLogger(ToolMagazineService.class);

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
        if (repository.existsById(tool.getId())) {
            throw new IllegalArgumentException("Tool with ID '" + tool.getId() + "' already exists!");
        }

        repository.save(tool);
        log.info("Tool added: id={}, type={}, quantity={}", tool.getId(), tool.getType(), tool.getQuantity());
    }

    public boolean removeTool(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Tool ID cannot be empty.");
        }
        boolean removed = repository.deleteById(id);
        if (removed) {
            log.info("Tool removed: id={}", id);
        } else {
            log.debug("Tool removal skipped, no such id: {}", id);
        }
        return removed;
    }

    public List<Tool> getAllTools() {
        return repository.findAll();
    }

    public boolean updateQuantity(String id, int quantity) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Tool ID cannot be empty.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        boolean updated = repository.updateQuantity(id, quantity);
        if (updated) {
            log.info("Tool quantity updated: id={}, newQuantity={}", id, quantity);
        } else {
            log.debug("Quantity update skipped, no such id: {}", id);
        }
        return updated;
    }

    public boolean issueReturnTool(String id, int amount, OperationType type) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Tool ID cannot be empty.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        boolean success = type == OperationType.PICKUP ? repository.issueTool(id, amount) : repository.returnTool(id, amount);
        if (success) {
            log.info("Tool: {}: id={}, amount={}", type == OperationType.PICKUP ? "issued" : "returned", id, amount);
        } else {
            log.debug("{} skipped, no such id of insufficient quantity: {}", type, id);
        }
        return success;
    }
}
