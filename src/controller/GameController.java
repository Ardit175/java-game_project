package controller;

import model.*;
import util.DatabaseConfig;

import java.sql.*;

public class GameController {
    private Maze maze;
    private Player player;
    private GameStatus status;
    private int currentLevel;
    private boolean isDirty;
    private int gameId;

    public GameController() {
        this.status = GameStatus.NOT_STARTED;
        this.currentLevel = 1;
        this.isDirty = false;
        this.gameId = -1;
    }


    public boolean movePlayer(Direction direction) {
        if (status != GameStatus.IN_PROGRESS) {
            return false;
        }

        int newX = player.getX();
        int newY = player.getY();

        switch (direction) {
            case UP:
                newY--;
                break;
            case DOWN:
                newY++;
                break;
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
        }

        if (maze.isWall(newX, newY)) {
            status = GameStatus.LOST;
            return false;
        }

        player.setX(newX);
        player.setY(newY);
        isDirty = true;

        // Kontrollojmë për thesar
        Cell currentCell = maze.getCell(newX, newY);
        if (currentCell.getType() == CellType.TREASURE) {
            Treasure treasure = currentCell.getTreasure();
            if (treasure != null) {
                player.collectTreasure(treasure.getPoints());
                currentCell.setType(CellType.PATH);
                currentCell.setTreasure(null);
            }
        }


        // Kontrollojmë për dalje
        if (maze.isExit(newX, newY)) {
            status = GameStatus.WON;
            player.completeLevel();  // Ruaj pikët e nivelit
            currentLevel++;
        }

        return true;
    }



    public String getLastMoveResult() {
        switch (status) {
            case LOST:
                return String.format(
                        "Game Over!\nNivele të përfunduara: %d\n" +
                                "Pikët në këtë nivel: %d\n" +
                                "Pikët totale: %d",
                        player.getCompletedLevels(),
                        player.getCurrentLevelScore(),
                        player.getTotalScore()
                );
            case WON:
                return String.format(
                        "Urime! Kaluat nivelin %d!\n" +
                                "Pikët në këtë nivel: %d\n" +
                                "Pikët totale: %d\n\n" +
                                "Doni të vazhdoni në nivelin %d?",
                        currentLevel,
                        player.getCurrentLevelScore(),
                        player.getTotalScore(),
                        currentLevel + 1
                );
            default:
                return null;
        }
    }

    public void saveHighScore() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO high_scores (user_id, score, levels_completed, level_scores) " +
                    "VALUES (?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int userId = auth.UserAuthentication.getUserId(player.getUsername());
                if (userId == -1) return;

                pstmt.setInt(1, userId);
                pstmt.setInt(2, player.getTotalScore());
                pstmt.setInt(3, player.getCompletedLevels());
                pstmt.setString(4, String.join(",",
                        player.getLevelScores().stream()
                                .map(String::valueOf)
                                .toArray(String[]::new)));

                pstmt.executeUpdate();
                System.out.println("Score saved: " + player.getTotalScore());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startNextLevel() {
        player.completeLevel();  // Kjo do të ruajë pikët e nivelit aktual
        currentLevel++;
        maze = new Maze(15, 15);
        player.setX(maze.getStartX());
        player.setY(maze.getStartY());
        status = GameStatus.IN_PROGRESS;
    }

    public void startNewGame(String username) {
        maze = new Maze(15, 15);
        player = new Player(username, maze.getStartX(), maze.getStartY());
        status = GameStatus.IN_PROGRESS;
        currentLevel = 1;
        isDirty = false;
    }


    public boolean saveGame() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            int userId = auth.UserAuthentication.getUserId(player.getUsername());
            if (userId == -1) {
                throw new SQLException("Invalid user ID");
            }

            String sql = "INSERT INTO saved_games (user_id, maze_data, player_position_x, " +
                    "player_position_y, collected_treasures, score, game_status, current_level) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, userId);
            pstmt.setString(2, maze.serializeMaze());
            pstmt.setInt(3, player.getX());
            pstmt.setInt(4, player.getY());
            pstmt.setInt(5, player.getCollectedTreasures());
            pstmt.setInt(6, player.getTotalScore());
            pstmt.setString(7, status.name());
            pstmt.setInt(8, currentLevel);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    gameId = rs.getInt(1);
                    conn.commit();
                    isDirty = false;
                    return true;
                }
            }

            conn.rollback();
            return false;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean loadGame(int gameId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            String sql = "SELECT * FROM saved_games WHERE game_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, gameId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                this.gameId = gameId;
                maze = Maze.deserializeMaze(rs.getString("maze_data"));

                if (player == null) {
                    player = new Player("", rs.getInt("player_position_x"),
                            rs.getInt("player_position_y"));
                } else {
                    player.setX(rs.getInt("player_position_x"));
                    player.setY(rs.getInt("player_position_y"));
                }

                player.resetForNewGame();
                player.collectTreasure(rs.getInt("score")); // Set current score
                currentLevel = rs.getInt("current_level");
                status = GameStatus.valueOf(rs.getString("game_status"));
                isDirty = false;
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // Getters
    public Maze getMaze() {
        return maze;
    }

    public Player getPlayer() {
        return player;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public boolean isGameActive() {
        return status == GameStatus.IN_PROGRESS;
    }

    public boolean hasUnsavedChanges() {
        return isDirty;
    }

    public int getGameId() {
        return gameId;
    }
}
