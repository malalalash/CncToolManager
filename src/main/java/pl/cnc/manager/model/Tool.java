package pl.cnc.manager.model;

import java.util.Locale;
import java.util.Objects;

public sealed abstract class Tool implements Comparable<Tool> permits Drill, EndMill, FaceMill, Tap {
    private final ToolType type;
    private final String id;
    private final String name;
    private int quantity;
    private final double diameter;

    public Tool(ToolType type, String id, String name, double diameter, int quantity) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Tool ID cannot be empty.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (diameter <= 0) {
            throw new IllegalArgumentException("Diameter must be greater than 0");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        this.type = type;
        this.id = id;
        this.name = name;
        this.diameter = diameter;
        this.quantity = quantity;
    }

    protected abstract String getExtraDetails();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDiameter() {
        return diameter;
    }

    public int getQuantity() {
        return quantity;
    }

    public ToolType getType() {
        return type;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        String extra = getExtraDetails();
        String extraFormatted = (extra == null || extra.isEmpty()) ? "" : extra + "\n";

        return String.format(Locale.ENGLISH,
                "### %s ###\nid: %s\nname: %s\ndiameter: %.2f\n%squantity: %d",
                type, id, name, diameter, extraFormatted, quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Tool other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare to null");
        }
        if (this.id.equals(other.id)) {
            return 0;
        }

        int result = Double.compare(this.diameter, other.diameter);
        if (result != 0) {
            return result;
        }
        return this.id.compareTo(other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tool other = (Tool) o;
        return Objects.equals(id, other.id);
    }
}
