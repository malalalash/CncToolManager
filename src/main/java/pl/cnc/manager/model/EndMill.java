package pl.cnc.manager.model;

import java.util.Locale;

public class EndMill extends Tool {
    private final int flutes;

    public EndMill(String id, String name, double diameter, int flutes, int quantity) {
        super(ToolType.END_MILL, id, name, diameter, quantity);
        if (flutes <= 0) {
            throw new IllegalArgumentException("Flutes must be greater than 0.");
        }
        this.flutes = flutes;
    }

    public int getFlutes() {
        return flutes;
    }

    @Override
    public String toCsv() {
        return String.format(Locale.ENGLISH, "%s,%s,%s,%.2f,%d,%d",
                getType(), getId(), getName(), getDiameter(), getFlutes(), getQuantity());
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "### END_MILL ###\nid: %s\nname: %s\ndiameter: %.2f\nflutes: %d\nquantity: %d",
                getId(), getName(), getDiameter(), flutes, getQuantity());
    }
}
