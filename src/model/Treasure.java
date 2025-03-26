package model;

public class Treasure {
    private TreasureType type;
    private int points;
    private String description;

    public Treasure(TreasureType type, int points, String description) {
        this.type = type;
        this.points = points;
        this.description = description;
    }

    public TreasureType getType() { return type; }
    public int getPoints() { return points; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("%s (%d pikë)", description, points);
    }
}
