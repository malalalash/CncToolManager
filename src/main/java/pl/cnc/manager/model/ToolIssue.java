package pl.cnc.manager.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record ToolIssue(
        String id,
        String toolId,
        String toolName,
        ToolType toolType,
        int amount,
        OperationType operationType,
        LocalDateTime issuedAt
) {
    public ToolIssue {
        if (toolId == null || toolId.isBlank()) {
            throw new IllegalArgumentException("Tool ID cannot be empty.");
        }
        if (toolName == null || toolName.isBlank()) {
            throw new IllegalArgumentException("Tool name cannot be empty.");
        }
        if (toolType == null) {
            throw new IllegalArgumentException("Tool type cannot be null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (operationType == null) {
            throw new IllegalArgumentException("Operation type cannot be null.");
        }
        if (issuedAt == null) {
            throw new IllegalArgumentException("Issued date cannot be null.");
        }
    }

    @Override
    public String toString() {
        String type = operationType == OperationType.PICKUP ? "issued" : "returned";
        String dateText = issuedAt.format(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm"));
        return String.format(Locale.ENGLISH,
                "[%s] %s: %d x %s (%s, id: %s)",
                dateText, type, amount, toolName, toolType, toolId);
    }
}
