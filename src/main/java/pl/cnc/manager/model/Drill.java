package pl.cnc.manager.model;

public final class Drill extends Tool {
    public Drill(String id, String name, double diameter, int quantity) {
        super(ToolType.DRILL, id, name, diameter, quantity);
    }
}
