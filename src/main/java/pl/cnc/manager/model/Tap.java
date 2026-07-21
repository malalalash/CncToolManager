package pl.cnc.manager.model;

import java.util.Locale;

public final class Tap extends Tool {
    private final double pitch;

    public Tap(String id, String name, double diameter, double pitch, int quantity) {
        super(ToolType.TAP, id, name, diameter, quantity);
        if (pitch <= 0) {
            throw new IllegalArgumentException("Pitch must be greater than 0");
        }
        this.pitch = pitch;
    }

    public double getPitch() {
        return pitch;
    }

    @Override
    protected String getExtraDetails() {
        return String.format(Locale.ENGLISH, "pitch: %.2f", pitch);
    }
}