package pl.cnc.manager.model;

import java.util.Locale;

public final class EndMill extends Tool {
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
    protected String getExtraDetails() {
        return String.format(Locale.ENGLISH, "flutes: %d", flutes);
    }
}
