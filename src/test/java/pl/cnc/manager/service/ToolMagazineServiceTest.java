package pl.cnc.manager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cnc.manager.model.*;
import pl.cnc.manager.repository.ToolRepository;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolMagazineServiceTest {

    @Mock
    private ToolRepository repository;

    private ToolMagazineService service;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        service = new ToolMagazineService(repository);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private Scanner createMockScanner(String input) {
        return new Scanner(input);
    }

    @Test
    void addTool_shouldAddDrillSuccessfully() {
        String simulatedInput = "1\nT1\nWiertlo\n10.5\n5\n";
        Scanner scanner = createMockScanner(simulatedInput);

        when(repository.existById("T1")).thenReturn(false);

        service.addTool(scanner);

        ArgumentCaptor<Tool> toolCaptor = ArgumentCaptor.forClass(Tool.class);
        verify(repository, times(1)).save(toolCaptor.capture());

        Tool savedTool = toolCaptor.getValue();
        assertInstanceOf(Drill.class, savedTool);
        assertEquals("T1", savedTool.getId());
        assertEquals("Wiertlo", savedTool.getName());
        assertEquals(10.5, savedTool.getDiameter());
        assertEquals(5, savedTool.getQuantity());
    }

    @Test
    void addTool_shouldFailWhenIdAlreadyExists() {
        String simulatedInput = "1\nT1\n";
        Scanner scanner = createMockScanner(simulatedInput);

        when(repository.existById("T1")).thenReturn(true);

        service.addTool(scanner);

        verify(repository, never()).save(any());
        assertTrue(outputStreamCaptor.toString().contains("Tool with ID 'T1' already exists!"));
    }

    @Test
    void addTool_shouldFailWhenInvalidNumberEntered() {
        String simulatedInput = "1\nT1\nWiertlo\nNOT_A_DOUBLE\n";
        Scanner scanner = createMockScanner(simulatedInput);

        when(repository.existById("T1")).thenReturn(false);

        service.addTool(scanner);

        verify(repository, never()).save(any());
        assertTrue(outputStreamCaptor.toString().contains("Invalid number entered. Tool not added."));
    }

    @Test
    void addTool_shouldFailWhenInvalidTypeSelected() {
        String simulatedInput = "99\n";
        Scanner scanner = createMockScanner(simulatedInput);

        service.addTool(scanner);

        verify(repository, never()).save(any());
        assertTrue(outputStreamCaptor.toString().contains("Invalid tool type selection."));
    }

    @Test
    void addTool_shouldFailWhenIdIsEmpty() {
        String simulatedInput = "1\n\n";
        Scanner scanner = createMockScanner(simulatedInput);

        service.addTool(scanner);

        verify(repository, never()).save(any());
        assertFalse(outputStreamCaptor.toString().contains("Error: Tool with ID '' already exists!"));
    }

    @Test
    void removeTool_shouldSuccessWhenToolExists() {
        String simulatedInput = "T1\n";
        Scanner scanner = createMockScanner(simulatedInput);

        when(repository.deleteById("T1")).thenReturn(true);

        service.removeTool(scanner);

        verify(repository, times(1)).deleteById("T1");
        assertTrue(outputStreamCaptor.toString().contains("Tool with id: T1 has been removed."));
    }

    @Test
    void removeTool_shouldNotifyWhenToolDoesNotExist() {
        String simulatedInput = "T1\n";
        Scanner scanner = createMockScanner(simulatedInput);

        when(repository.deleteById("T1")).thenReturn(false);

        service.removeTool(scanner);

        verify(repository, times(1)).deleteById("T1");
        assertTrue(outputStreamCaptor.toString().contains("No tool found with id: T1"));
    }

    @Test
    void listTools_shouldPrintEmptyMessageWhenNoTools() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        service.listTools();

        assertTrue(outputStreamCaptor.toString().contains("Magazine is empty."));
    }

    @Test
    void listTools_shouldPrintToolsWhenInventoryHasItems() {
        Drill drill = new Drill("T1", "Wiertlo", 10.0, 5);
        when(repository.findAll()).thenReturn(List.of(drill));

        service.listTools();

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("--- INVENTORY ---"));
        assertTrue(output.contains("T1"));
    }
}