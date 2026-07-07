package pl.cnc.manager.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EndMillTest {
    @Test
    void shouldCreateEndMillCorrectly() {
        EndMill mill = new EndMill("EM-1","End Mill 4", 4, 3,1);

        assertAll(
                () -> assertEquals(ToolType.END_MILL, mill.getType()),
                () -> assertEquals("EM-1", mill.getId()),
                () -> assertEquals("End Mill 4", mill.getName()),
                () -> assertEquals(4, mill.getDiameter()),
                () -> assertEquals(3, mill.getFlutes()),
                () -> assertEquals(1, mill.getQuantity())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5, -100})
    void shouldThrowExceptionForInvalidFlutes(int flutes) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EndMill("E1", "End Mill", 10.0, flutes, 5)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5, -100})
    void shouldThrowExceptionForInvalidDiameters(int diameter) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EndMill("E1", "End Mill", 10.0, diameter, 5)
        );
    }
    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    void shouldThrowExceptionForInvalidQuantity(int quantity) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EndMill("E001", "End Mill", 10.0, 4, quantity)
        );
    }
    @Test
    void shouldSetQuantity() {
        EndMill mill = new EndMill("E001", "End Mill", 10.0, 4, 5);

        mill.setQuantity(20);

        assertEquals(20, mill.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenSettingNegativeQuantity() {
        EndMill mill = new EndMill("E001", "End Mill", 10.0, 4, 5);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mill.setQuantity(-1)
        );

        assertEquals("Quantity cannot be negative", exception.getMessage());
    }
    @Test
    void shouldReturnCorrectCsv() {
        EndMill mill = new EndMill("E001", "Carbide End Mill", 12.0, 4, 10);

        String expected = "END_MILL,E001,Carbide End Mill,12.00,4,10";

        assertEquals(expected, mill.toCsv());
    }
    @Test
    void shouldReturnCorrectToString() {
        EndMill mill = new EndMill("E001", "Carbide End Mill", 12.0, 4, 10);

        String expected = """
                ### END_MILL ###
                id: E001
                name: Carbide End Mill
                diameter: 12.00
                flutes: 4
                quantity: 10""";

        assertEquals(expected, mill.toString());
    }
}