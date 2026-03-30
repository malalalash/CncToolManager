package pl.cnc.manager.model;

import java.util.Locale;

public class Drill extends Tool {
    public Drill(String id, String name, double diameter, int quantity) {
        super(id, name, diameter, quantity);
    }

    @Override
    public String toCsv() {
        return String.format(Locale.ENGLISH, "DRILL,%s,%s,%.2f,%d", getId(), getName(), getDiameter(), getQuantity());
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "### DRILL ###\nid: %s\nname: %s\ndiameter: %.2f\nquantity: %s", getId(), getName(), getDiameter(), getQuantity());
    }
}
