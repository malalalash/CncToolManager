package pl.cnc.manager.model;

import pl.cnc.manager.util.Validation;

import java.util.Locale;

public final class Tap extends Tool {
    private final double pitch;

    public Tap(String id, String name, double diameter, double pitch, int quantity) {
        super(ToolType.TAP, id, name, diameter, quantity);
        Validation.requirePositive(pitch, "Pitch must be greater than 0");
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