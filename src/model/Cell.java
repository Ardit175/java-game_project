package model;

public class Cell {
    private CellType type;
    private Treasure treasure;

    public Cell(CellType type) {
        this.type = type;
        this.treasure = null;
    }

    public boolean isWall() {
        return type == CellType.WALL;
    }

    public boolean isTreasure() {
        return type == CellType.TREASURE;
    }

    public boolean isExit() {
        return type == CellType.EXIT;
    }

    public void setTreasure(Treasure treasure) {
        this.treasure = treasure;
        if (treasure != null) {
            this.type = CellType.TREASURE;
        }
    }

    public Treasure getTreasure() {
        return treasure;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
        if (type != CellType.TREASURE) {
            this.treasure = null;
        }
    }
}