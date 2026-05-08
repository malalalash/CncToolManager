package pl.cnc.manager.model;

public abstract class Tool {
    private final ToolType type;
    private final String id;
    private final String name;
    private int quantity;
    private final double diameter;

    public Tool(Enum<ToolType> type, String id, String name, double diameter, int quantity) {
        this.type = (ToolType) type;
        this.id = id;
        this.name = name;
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
        if (diameter <= 0) {
            throw new IllegalArgumentException("Diameter must be greater than 0.");
        }
        this.diameter = diameter;
    }

    public abstract String toCsv();

    public ToolType getType() { return type; }

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
            throw new IllegalArgumentException("Quantity can't be negative.");
        }
        this.quantity = quantity;
    }
    public abstract String toString();
}
