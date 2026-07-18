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
        void shouldBeEqualWhenIdIsTheSame() {
            Tool t1 = new EndMill("E1", "Mill", 12.00, 3, 2);
            Tool t2 = new EndMill("E1", "Mill2", 6.00, 4, 0);

            assertEquals(t1, t2);
            assertEquals(t1.hashCode(), t2.hashCode(), "hashCode() has to be equal");
        }

        @Test
        @DisplayName("Should not be equal when ids are different")
        void shouldNotBeEqualWhenIdsAreDifferent() {
            Tool t1 = new EndMill("E1", "Mill", 12.00, 3, 2);
            Tool t2 = new EndMill("E2", "Mill2", 6.00, 4, 0);

            assertNotEquals(t1, t2);
        }

        @Test
        @DisplayName("Should respect standard equals contract")
        void shouldRespectStandardEqualsContract() {
            Tool t1 = new EndMill("E1", "Mill", 12.00, 3, 2);

            assertEquals(t1, t1, "Object should be equal to itself");
            assertNotEquals(null, t1, "Object should not be equal to null");
            assertNotEquals("Some string", t1, "Object should not be equal to object of other class");
        }
    }

    @Nested
    @DisplayName("Comparable interface tests")
    class ComparableTests {
        @Test
        @DisplayName("Should return negative value when first tool is smaller")
        void shouldReturnNegativeWhenFirstIsSmaller() {
            Tool smallTool = new EndMill("E1", "Mill2", 6.00, 4, 0);
            Tool bigTool = new EndMill("E2", "Mill", 12.00, 3, 2);

            assertTrue(smallTool.compareTo(bigTool) < 0);
        }

        @Test
        @DisplayName("Should return positive value when first tool is larger")
        void shouldReturnPositiveWhenFirstIsLarger() {
            Tool bigTool = new EndMill("E1", "Mill", 12.00, 3, 2);
            Tool smallTool = new EndMill("E2", "Mill2", 6.00, 4, 0);


            assertTrue(bigTool.compareTo(smallTool) > 0);
        }

        @Test
        @DisplayName("Should compare by id when diameters are equal")
        void shouldCompareByIdWhenDiametersAreEqual() {
            Tool t1 = new EndMill("E1", "Mill", 12.00, 3, 2);
            Tool t2 = new EndMill("E2", "Mill2", 12.00, 4, 0);

            assertTrue(t1.compareTo(t2) < 0);
            assertTrue(t2.compareTo(t1) > 0);
        }

        @Test
        @DisplayName("Should return 0 when diameter and id are the same")
        void shouldReturnZeroWhenSameIdAndDiameter() {
            Tool t1 = new EndMill("E1", "Mill", 12.00, 3, 2);
            Tool t2 = new EndMill("E1", "Mill2", 12.00, 9, 0);

            assertEquals(0, t1.compareTo(t2));
        }

        @Test
        @DisplayName("Should throw exception when comparing to null")
        void shouldThrowExceptionWhenComparingToNull() {
            Tool t1 = new EndMill("E1", "Mill", 12.00, 3, 2);

            assertThrows(NullPointerException.class, () -> t1.compareTo(null));
        }
    }
}