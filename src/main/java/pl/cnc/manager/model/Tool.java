package pl.cnc.manager.model;

public abstract class Tool {
    private String id;
    private String name;
    private double diameter;

    public Tool(String id, String name, double diameter) {
        this.id = id;
        this.name = name;
        if (diameter <= 0) {
            throw new IllegalArgumentException("Diameter must be greater than 0.");
        }
        this.diameter = diameter;
    }

    public abstract String toCsv();

    public String getId() {
        return id;
    }

    ;

    public String getName() {
        return name;
    }

    ;

    public double getDiameter() {
        return diameter;
    }

    ;

    public abstract String toString();
}
