package model;

public enum TreasureType {
    COIN("Monedhë ari", 10),
    GEM("Gur i çmuar", 50),
    CHEST("Arkë thesari", 100),
    CROWN("Kurorë mbretërore", 200);

    private final String name;
    private final int defaultPoints;

    TreasureType(String name, int defaultPoints) {
        this.name = name;
        this.defaultPoints = defaultPoints;
    }

    public String getName() { return name; }
    public int getDefaultPoints() { return defaultPoints; }
}
