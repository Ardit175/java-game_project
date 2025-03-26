package events;

import controller.GameStatus;

public interface GameEventListener {
    void onGameStatusChanged(GameStatus newStatus);
    void onScoreChanged(int newScore);
    void onTreasureCollected(int treasurePoints);
}
