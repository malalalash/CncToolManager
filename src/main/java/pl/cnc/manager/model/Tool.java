package pl.cnc.manager.model;

public abstract class Tool {
    private final String id;
    private final String name;
    private int quantity;
    private double diameter;

    public Tool(String id, String name, double diameter) {
        this.id = id;
        this.name = name;
        this.quantity = 1;
        if (diameter <= 0) {
            throw new IllegalArgumentException("Diameter must be greater than 0.");
        }
        this.diameter = diameter;
    }

    public abstract String toCsv();

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
        this.quantity = quantity;
    }
    public abstract String toString();
}
