package pl.cnc.manager.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FaceMillTest {
    @Test
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
    @ValueSource(ints = {0, -1, -5})
    void shouldThrowExceptionForInvalidInserts(int inserts) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FaceMill("FM1", "FaceMill 40mm", 40, inserts, 2)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    void shouldThrowExceptionForInvalidDiameter(int diameter) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FaceMill("FM1", "FaceMill 40mm", diameter, 6, 2)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, -5})
    void shouldThrowExceptionForInvalidQuantity(int quantity) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FaceMill("FM1", "FaceMill 40mm", 40, 6, quantity)
        );
    }

    @Test
    void shouldSetQuantity() {
        FaceMill faceMill = new FaceMill("FM1", "FaceMill 40mm", 40, 6, 2);

        faceMill.setQuantity(100);

        assertEquals(100, faceMill.getQuantity());
    }
}