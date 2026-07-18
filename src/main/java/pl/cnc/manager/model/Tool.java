package pl.cnc.manager.model;

import java.util.Locale;
import java.util.Objects;

public abstract class Tool implements Comparable<Tool> {
    private final ToolType type;
    private final String id;
    private final String name;
    private int quantity;
    private final double diameter;

    public Tool(ToolType type, String id, String name, double diameter, int quantity) {
        this.type = type;
        this.id = id;
        this.name = name;
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
        if (diameter <= 0) {
            throw new IllegalArgumentException("Diameter must be greater than 0");
        }
        this.diameter = diameter;
    }

    public ToolType getType() {
        return type;
    }

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

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "### %s ###\nid: %s\nname: %s\ndiameter: %.2f\nquantity: %d",
                type, id, name, diameter, quantity);
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
        return Double.compare(this.diameter, other.diameter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tool other = (Tool) o;
        return Objects.equals(id, other.id);
    }
}
