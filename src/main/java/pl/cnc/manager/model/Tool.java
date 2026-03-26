package pl.cnc.manager.model;

public abstract class Tool {
    private String id;
    private String name;
    private double diameter;

    public Tool(String id, String name, double diameter) {
        this.id = id;
        this.name = name;
        this.diameter = diameter;
    }

    public String getId() {return id;};
    public String getName() {return name;};
    public double getDiameter() {return diameter;};
}
