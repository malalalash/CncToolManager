package pl.cnc.manager.model;

import java.util.Locale;

public class Tap extends Tool {
    private final double pitch;

    public Tap(String id, String name, double diameter, double pitch, int quantity) {
        super(ToolType.TAP, id, name, diameter, quantity);
        if (pitch <= 0) {
            throw new IllegalArgumentException("pitch must be greater than 0.");
        }
        this.pitch = pitch;
    }

    public double getPitch() { return pitch; }

    @Override
    public String toCsv() {
        return String.format(Locale.ENGLISH, "%s,%s,%s,%.2f,%.2f,%d",
                getType(), getId(), getName(), getDiameter(), getPitch(), getQuantity());
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "### TAP ###\nid: %s\nname: %s\ndiameter: %.2f\nflutes: %.2f\nquantity: %d",
                getId(), getName(), getDiameter(), getPitch(), getQuantity());
    }
}