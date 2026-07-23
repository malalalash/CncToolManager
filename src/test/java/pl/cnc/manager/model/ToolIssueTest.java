package pl.cnc.manager.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ToolIssueTest {

    private ToolIssue validIssue() {
        return new ToolIssue("1", "D1", "Drill 8mm", ToolType.DRILL, 3, OperationType.PICKUP, LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create valid tool issue")
    void shouldCreateValidToolIssue(){
        ToolIssue issue = validIssue();
        assertEquals("D1", issue.toolId());
        assertEquals(3, issue.amount());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when tool id is blank")
    void shouldThrowWhenToolIdIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new ToolIssue("1", " ", "Drill 8mm", ToolType.DRILL, 3, OperationType.PICKUP, LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when tool name is blank")
    void shouldThrowWhenToolNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new ToolIssue("1", "D!", " ", ToolType.DRILL, 3, OperationType.PICKUP, LocalDateTime.now()));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, 0})
    @DisplayName("Should throw IllegalArgumentException when amount is not positive")
    void shouldThrowWhenAmountIsNotPositive(int amount) {
        assertThrows(IllegalArgumentException.class,
                () -> new ToolIssue("1", "D1", "Drill 8mm", ToolType.DRILL, amount, OperationType.PICKUP, LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when tool type is null")
    void shouldThrowWhenToolTypeIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ToolIssue("1", "D1", "Drill 8 mm", null, 3, OperationType.PICKUP, LocalDateTime.now()));
    }
    @Test
    @DisplayName("Should throw IllegalArgumentException when operation type is null")
    void shouldThrowWhenOperationTypeIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ToolIssue("1", "D1", "Drill 8 mm", ToolType.DRILL, 3, null, LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when issued at is null")
    void shouldThrowWhenIssuedAtIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ToolIssue("1", "D1", "Drill 8 mm", ToolType.DRILL, 3, OperationType.PICKUP, null));
    }

    @Test
    @DisplayName("toString() should format issue with readable date and action")
    void shouldFormatToStringCorrectly() {
        LocalDateTime issuedAt = LocalDateTime.of(2026, 7, 23, 14, 32);
        ToolIssue issue = new ToolIssue("1", "D1", "Drill 8mm", ToolType.DRILL,
                2, OperationType.PICKUP, issuedAt);

        String result = issue.toString();

        assertEquals("[23-07-2026 14:32] issued: 2 x Drill 8mm (DRILL, id: D1)", result);
    }
}