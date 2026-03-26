package pl.cnc.manager.model;

public class Drill extends Tool {
    public Drill(String id, String name, double diameter) {
        super(id, name, diameter);
    }

    @Override
    public String toCsv() {
        return "DRILL," + getId() + "," + getName() + "," + getDiameter();
    }

    @Override
    public String toString() {
        return "### DRILL ###\n" + "id: " + getId() + "\nname: " + getName() + "\nDiameter: " + getDiameter();
    }
}
