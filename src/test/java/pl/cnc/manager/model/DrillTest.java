package pl.cnc.manager.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrillTest {

    @Test
    void shouldCreateDrillCorrectly() {
        Drill drill = new Drill("D-01", "HSS Drill", 10.5, 5);

        assertEquals(ToolType.DRILL, drill.getType());
        assertEquals("D-01", drill.getId());
        assertEquals("HSS Drill", drill.getName());
        assertEquals(10.5, drill.getDiameter());
        assertEquals(5, drill.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Drill("D01", "Drill", 10.0, -1)
        );

        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDiameterIsZero() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Drill("D01", "Drill", 0, 5)
        );

        assertEquals("Diameter must be greater than 0", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenDiameterIsNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Drill("D001", "Drill", -5.0, 5)
        );
    }

    @Test
    void shouldSetQuantity() {
        Drill drill = new Drill("D001", "Drill", 8.0, 2);

        drill.setQuantity(10);

        assertEquals(10, drill.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenSettingNegativeQuantity() {
        Drill drill = new Drill("D1", "Drill", 9.0, 2);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> drill.setQuantity(-5)
        );

        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectToString() {
        Drill drill = new Drill("D001", "HSS Drill", 10.5, 5);

        String expected = """
                ### DRILL ###
                id: D001
                name: HSS Drill
                diameter: 10.50
                quantity: 5""";

        assertEquals(expected, drill.toString());
    }
}
