package pl.cnc.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.cnc.manager.model.*;
import pl.cnc.manager.repository.ToolRepository;
import pl.cnc.manager.util.Validation;

import java.util.List;

public class ToolMagazineService {
    private static final Logger log = LoggerFactory.getLogger(ToolMagazineService.class);

    private final ToolRepository repository;

    public ToolMagazineService(ToolRepository repository) {
        this.repository = repository;
    }

    public void addTool(Tool tool) {
        Validation.requireNonNull(tool, "Tool details cannot be null.");
        Validation.requireNonBlank(tool.getId(), "Tool ID cannot be empty.");
        if (repository.existsById(tool.getId())) {
            throw new IllegalArgumentException("Tool with ID '" + tool.getId() + "' already exists!");
        }

        repository.save(tool);
        log.info("Tool added: id={}, type={}, quantity={}", tool.getId(), tool.getType(), tool.getQuantity());
    }

    public boolean removeTool(String id) {
        Validation.requireNonBlank(id, "Tool ID cannot be empty.");
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
        Validation.requireNonBlank(id, "Tool ID cannot be empty.");
        Validation.requireNonNegative(quantity, "Quantity cannot be negative.");
        boolean updated = repository.updateQuantity(id, quantity);
        if (updated) {
            log.info("Tool quantity updated: id={}, newQuantity={}", id, quantity);
        } else {
            log.debug("Quantity update skipped, no such id: {}", id);
        }
        return updated;
    }

    public boolean issueReturnTool(String id, int amount, OperationType type) {
        Validation.requireNonBlank(id, "Tool ID cannot be empty.");
        Validation.requirePositive(amount, "Amount must be positive.");
        boolean success = type == OperationType.PICKUP ? repository.issueTool(id, amount) : repository.returnTool(id, amount);
        if (success) {
            log.info("Tool: {}: id={}, amount={}", type == OperationType.PICKUP ? "issued" : "returned", id, amount);
        } else {
            log.debug("{} skipped, no such id of insufficient quantity: {}", type, id);
        }
        return success;
    }
}
