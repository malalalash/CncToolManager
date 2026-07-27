package pl.cnc.manager.model;

import pl.cnc.manager.util.Validation;

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
        Validation.requireNonBlank(toolId, "Tool ID cannot be empty.");
        Validation.requireNonBlank(toolName, "Tool name cannot be empty.");
        Validation.requireNonNull(toolType, "Tool type cannot be null.");
        Validation.requirePositive(amount, "Amount must be positive");
        Validation.requireNonNull(operationType, "Operation type cannot be null.");
        Validation.requireNonNull(issuedAt, "Issued date cannot be null.");
    }

    @Override
    public String toString() {
        String type = operationType == OperationType.PICKUP ? "issued" : "returned";
        String dateText = issuedAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return String.format(Locale.ENGLISH,
                "[%s] %s: %d x %s (%s, id: %s)",
                dateText, type, amount, toolName, toolType, toolId);
    }
}
