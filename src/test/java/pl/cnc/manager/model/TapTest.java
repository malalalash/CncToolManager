package pl.cnc.manager.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TapTest {
    @Test
    void shouldCreateTapCorrectly() {
        Tap tap = new Tap("T-01", "M10", 10.0, 1.5, 3);

        assertAll(
                () -> assertEquals(ToolType.TAP, tap.getType()),
                () -> assertEquals("T-01", tap.getId()),
                () -> assertEquals("M10", tap.getName()),
                () -> assertEquals(10.0, tap.getDiameter()),
                () -> assertEquals(1.5, tap.getPitch()),
                () -> assertEquals(3, tap.getQuantity())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -3})
    void shouldThrowExceptionForInvalidDiameter(int diameter) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Tap("T-01", "M10", diameter, 1.5, 3)
        );
        assertEquals("Diameter must be greater than 0", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -5.0, -2.25})
    void shouldThrowExceptionForInvalidPitch(double pitch) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Tap("T-01", "M10", 10.0, pitch, 3)
        );
        assertEquals("Pitch must be greater than 0", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    void shouldThrowExceptionForInvalidQuantity(int quantity) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Tap("T-01", "M10", 10.0, 1.5, quantity)
        );
        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSettingNegativeQuantity() {
        Tap tap = new Tap("T-01", "M10", 10.0, 1.5, 3);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> tap.setQuantity(-1)
        );

        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectToString() {
        Tap tap = new Tap("T-01", "M10", 10.0, 1.5, 2);

        String expected = """
                ### TAP ###
                id: T-01
                name: M10
                diameter: 10.00
                pitch: 1.50
                quantity: 2""";

        assertEquals(expected, tap.toString());
    }
}