package pl.cnc.manager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cnc.manager.model.Drill;
import pl.cnc.manager.model.Tool;
import pl.cnc.manager.repository.ToolRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolMagazineServiceTest {

    @Mock
    private ToolRepository repository;

    @InjectMocks
    private ToolMagazineService service;

    private Tool sampleTool;

    @BeforeEach
    void setUp() {
        sampleTool = new Drill("D-101", "Drill 10mm", 10.0, 5);
    }

    @Nested
    @DisplayName("Tests for addTool")
    class AddToolTests {

        @Test
        @DisplayName("Should successfully add a tool when it is valid and does not exist")
        void shouldAddToolSuccessfully() {
            when(repository.existsById("D-101")).thenReturn(false);

            assertDoesNotThrow(() -> service.addTool(sampleTool));

            verify(repository, times(1)).existsById("D-101");
            verify(repository, times(1)).save(sampleTool);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when the tool is null")
        void shouldThrowExceptionWhenToolIsNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.addTool(null)
            );

            assertEquals("Tool details cannot be null.", exception.getMessage());
            verifyNoInteractions(repository);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when tool ID is blank")
        void shouldThrowExceptionWhenIdIsBlank() {
            Tool invalidTool = new Drill("  ", "Drill 10mm", 10.0, 5);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.addTool(invalidTool)
            );

            assertEquals("Tool ID cannot be empty.", exception.getMessage());
            verifyNoInteractions(repository);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when tool ID already exists")
        void shouldThrowExceptionWhenToolAlreadyExists() {
            when(repository.existsById("D-101")).thenReturn(true);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.addTool(sampleTool)
            );

            assertEquals("Tool with ID 'D-101' already exists!", exception.getMessage());
            verify(repository, times(1)).existsById("D-101");
            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests for removeTool")
    class RemoveToolTests {

        @Test
        @DisplayName("Should return true when tool is successfully deleted")
        void shouldReturnTrueOnSuccessfulDeletion() {
            when(repository.deleteById("D-101")).thenReturn(true);

            boolean result = service.removeTool("D-101");

            assertTrue(result);
            verify(repository, times(1)).deleteById("D-101");
        }

        @Test
        @DisplayName("Should return false when tool to delete does not exist")
        void shouldReturnFalseWhenToolDoesNotExist() {
            when(repository.deleteById("NON-EXISTENT")).thenReturn(false);

            boolean result = service.removeTool("NON-EXISTENT");

            assertFalse(result);
            verify(repository, times(1)).deleteById("NON-EXISTENT");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when ID is blank")
        void shouldThrowExceptionWhenRemovingWithBlankId() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.removeTool("")
            );

            assertEquals("Tool ID cannot be empty.", exception.getMessage());
            verifyNoInteractions(repository);
        }
    }

    @Nested
    @DisplayName("Tests for getAllTools")
    class GetAllToolsTests {

        @Test
        @DisplayName("Should return a list of tools from the repository")
        void shouldReturnListOfTools() {
            List<Tool> expectedTools = List.of(sampleTool);
            when(repository.findAll()).thenReturn(expectedTools);

            List<Tool> actualTools = service.getAllTools();

            assertEquals(1, actualTools.size());
            assertEquals(sampleTool, actualTools.getFirst());
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when repository is empty")
        void shouldReturnEmptyListWhenNoToolsExist() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<Tool> actualTools = service.getAllTools();

            assertTrue(actualTools.isEmpty());
            verify(repository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Tests for updateQuantity")
    class UpdateQuantityTests {

        @Test
        @DisplayName("Should return true when quantity is successfully updated")
        void shouldReturnTrueOnSuccessfulUpdate() {
            when(repository.updateQuantity("D-101", 20)).thenReturn(true);

            boolean result = service.updateQuantity("D-101", 20);

            assertTrue(result);
            verify(repository, times(1)).updateQuantity("D-101", 20);
        }

        @Test
        @DisplayName("Should return false when tool to update does not exist")
        void shouldReturnFalseWhenToolDoesNotExist() {
            when(repository.updateQuantity("NON-EXISTENT", 20)).thenReturn(false);

            boolean result = service.updateQuantity("NON-EXISTENT", 20);

            assertFalse(result);
            verify(repository, times(1)).updateQuantity("NON-EXISTENT", 20);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when ID is blank")
        void shouldThrowExceptionWhenIdIsBlank() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.updateQuantity("  ", 20)
            );

            assertEquals("Tool ID cannot be empty.", exception.getMessage());
            verifyNoInteractions(repository);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when quantity is negative")
        void shouldThrowExceptionWhenQuantityIsNegative() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.updateQuantity("D-101", -1)
            );

            assertEquals("Quantity cannot be negative.", exception.getMessage());
            verifyNoInteractions(repository);
        }
    }
}