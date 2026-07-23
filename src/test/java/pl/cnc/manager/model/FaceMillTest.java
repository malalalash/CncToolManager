package pl.cnc.manager.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FaceMillTest {
    @Test
    @DisplayName("Should correctly create FaceMill object")
    void shouldCreateFaceMillCorrectly() {
        FaceMill faceMill = new FaceMill("FM1", "FaceMill 40mm", 40, 6, 2);

        assertAll(
                () -> assertEquals(ToolType.FACE_MILL, faceMill.getType()),
                () -> assertEquals("FM1", faceMill.getId()),
                () -> assertEquals("FaceMill 40mm", faceMill.getName()),
                () -> assertEquals(40, faceMill.getDiameter()),
                () -> assertEquals(6, faceMill.getInserts()),
                () -> assertEquals(2, faceMill.getQuantity())
        );
    }

    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid inserts value")
    @ValueSource(ints = {0, -1, -5})
    void shouldThrowExceptionForInvalidInserts(int inserts) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FaceMill("FM1", "FaceMill 40mm", 40, inserts, 2)
        );
    }

    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid diameter value")
    @ValueSource(ints = {0, -1, -5})
    void shouldThrowExceptionForInvalidDiameter(int diameter) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FaceMill("FM1", "FaceMill 40mm", diameter, 6, 2)
        );
    }

    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid quantity value")
    @ValueSource(ints = {-100, -1, -5})
    void shouldThrowExceptionForInvalidQuantity(int quantity) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FaceMill("FM1", "FaceMill 40mm", 40, 6, quantity)
        );
    }

    @Test
    @DisplayName("Should set quantity properly")
    void shouldSetQuantity() {
        FaceMill faceMill = new FaceMill("FM1", "FaceMill 40mm", 40, 6, 2);

        faceMill.setQuantity(100);

        assertEquals(100, faceMill.getQuantity());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when setting negative quantity")
    void shouldThrowExceptionWhenSettingNegativeQuantity() {
        FaceMill faceMill = new FaceMill("FM1", "FaceMill 40mm", 40, 6, 2);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> faceMill.setQuantity(-1)
        );

        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    @Test
    @DisplayName("Should print correct string")
    void shouldReturnCorrectToString() {
        FaceMill faceMill = new FaceMill("FM1", "FaceMill 40mm", 40, 6, 2);

        String expected = """
                ### FACE_MILL ###
                id: FM1
                name: FaceMill 40mm
                diameter: 40.00
                inserts: 6
                quantity: 2""";

        assertEquals(expected, faceMill.toString());
    }
}