package pl.cnc.manager.model;

import java.util.Locale;

public class Drill extends Tool {
    public Drill(String id, String name, double diameter, int quantity) {
        super(ToolType.DRILL, id, name, diameter, quantity);
    }
}
