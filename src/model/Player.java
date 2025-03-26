package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int x;
    private int y;
    private int collectedTreasures;
    private int currentLevelScore;
    private int totalScore;
    private List<Integer> levelScores;
    private String username;

    public Player(String username, int startX, int startY) {
        this.username = username;
        this.x = startX;
        this.y = startY;
        this.collectedTreasures = 0;
        this.currentLevelScore = 0;
        this.totalScore = 0;
        this.levelScores = new ArrayList<>();
    }

    public void move(Direction direction) {
        switch (direction) {
            case UP: y--; break;
            case DOWN: y++; break;
            case LEFT: x--; break;
            case RIGHT: x++; break;
        }
    }

    public void collectTreasure(int points) {
        collectedTreasures++;
        currentLevelScore += points;
        totalScore += points; // Simply add points directly to total
    }

    private int calculateTotalScore() {
        return currentLevelScore + levelScores.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public void completeLevel() {
        levelScores.add(currentLevelScore);
        // No need to update totalScore here as it's already correct
        currentLevelScore = 0;
        collectedTreasures = 0;
    }

    public void resetForNewGame() {
        currentLevelScore = 0;
        totalScore = 0;
        collectedTreasures = 0;
        levelScores.clear();
    }

    // Position getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Game state getters and setters
    public int getCollectedTreasures() {
        return collectedTreasures;
    }

    public void setCollectedTreasures(int treasures) {
        this.collectedTreasures = treasures;
    }

    public int getCurrentLevelScore() {
        return currentLevelScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public List<Integer> getLevelScores() {
        return new ArrayList<>(levelScores);
    }

    public String getUsername() {
        return username;
    }

    public int getCompletedLevels() {
        return levelScores.size();
    }

    public String getLevelHistory() {
        StringBuilder history = new StringBuilder();
        for (int i = 0; i < levelScores.size(); i++) {
            history.append(String.format("Niveli %d: %d pikë\n",
                    i + 1, levelScores.get(i)));
        }
        if (currentLevelScore > 0) {
            history.append(String.format("Niveli aktual %d: %d pikë\n",
                    levelScores.size() + 1, currentLevelScore));
        }
        history.append(String.format("Totali: %d pikë", getTotalScore()));
        return history.toString();
    }

    public boolean isAtPosition(int checkX, int checkY) {
        return x == checkX && y == checkY;
    }
}