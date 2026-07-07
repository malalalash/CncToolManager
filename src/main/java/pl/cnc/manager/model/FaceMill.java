package pl.cnc.manager.model;

import java.util.Locale;

public class FaceMill extends Tool {
    private final int inserts;

    public FaceMill(String id, String name, double diameter, int inserts, int quantity) {
        super(ToolType.FACE_MILL, id, name, diameter, quantity);
        if (inserts <= 0) {
            throw new IllegalArgumentException("Inserts must be greater than 0.");
        }
        this.inserts = inserts;
    }

    public int getInserts() {
        return this.inserts;
    }

    @Override
    public String toCsv() {
        return String.format(Locale.ENGLISH, "%s,%s,%s,%.2f,%d,%d",
                getType(), getId(), getName(), getDiameter(), getInserts(), getQuantity());
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "### FACE_MILL ###\nid: %s\nname: %s\ndiameter: %.2f\ninserts: %d\nquantity: %d",
                getId(), getName(), getDiameter(), inserts, getQuantity());
    }

}
