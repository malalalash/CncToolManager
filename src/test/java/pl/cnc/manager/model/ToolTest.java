package pl.cnc.manager.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolTest {
    @Nested
    @DisplayName("equals() and hashCode() methods tests")
    class EqualsAndHashCodeTests {
        @Test
        @DisplayName("Should be equal when ids are the same")
        void shouldBeEqualWhenIdIsTheSame () {
            Tool t1 = new EndMill("E1", "Mill", 12.00, 3, 2);
            Tool t2 = new EndMill("E1", "Mill2", 6.00, 4, 0);

            assertEquals(t1,t2);
            assertEquals(t1.hashCode(), t2.hashCode());
        }
    }
}