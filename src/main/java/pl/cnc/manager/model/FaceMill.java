package pl.cnc.manager.model;

import pl.cnc.manager.util.Validation;

import java.util.Locale;

public final class FaceMill extends Tool {
    private final int inserts;

    public FaceMill(String id, String name, double diameter, int inserts, int quantity) {
        super(ToolType.FACE_MILL, id, name, diameter, quantity);
        Validation.requirePositive(inserts, "Inserts must be greater than 0.");
        this.inserts = inserts;
    }

    public int getInserts() {
        return this.inserts;
    }

    @Override
    protected String getExtraDetails() {
        return String.format(Locale.ENGLISH, "inserts: %d", inserts);
    }
}
