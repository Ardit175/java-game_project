package model;

import util.DatabaseConfig;
import java.sql.*;
import java.util.*;

public class Maze {
    private Cell[][] grid;
    private int width;
    private int height;
    private int startX;
    private int startY;
    private int exitX;
    private int exitY;
    private Random random;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width];
        this.random = new Random();
        initializeGrid();
    }

    private void initializeGrid() {
        do {
            // Fillimisht të gjitha qelizat janë mure
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    grid[y][x] = new Cell(CellType.WALL);
                }
            }

            // Gjenero labirintin
            startX = 1;
            startY = 1;
            generateMaze(startX, startY);

            // Vendos daljen
            placeExit();

        } while (!checkPath()); // Rigjenero nëse nuk ka rrugë

        // Vendos thesaret
        placeTreasures();

        // Shëno pikën e fillimit
        grid[startY][startX].setType(CellType.START);
    }

    private void generateMaze(int x, int y) {
        grid[y][x].setType(CellType.PATH);

        // Drejtimet e mundshme
        Direction[] directions = Direction.values();
        List<Direction> dirList = new ArrayList<>(Arrays.asList(directions));
        Collections.shuffle(dirList);

        for (Direction dir : dirList) {
            int nextX = x;
            int nextY = y;

            // Lëviz dy hapa në drejtimin e zgjedhur
            switch(dir) {
                case UP:    nextY -= 2; break;
                case DOWN:  nextY += 2; break;
                case LEFT:  nextX -= 2; break;
                case RIGHT: nextX += 2; break;
            }

            if (isValid(nextX, nextY) && grid[nextY][nextX].isWall()) {
                // Bëj PATH qelizën në mes
                grid[(y + nextY)/2][(x + nextX)/2].setType(CellType.PATH);
                generateMaze(nextX, nextY);
            }
        }
    }

    // Sigurohu që ka rrugë nga fillimi te dalja
    private boolean checkPath() {
        boolean[][] visited = new boolean[height][width];
        return hasPath(startX, startY, exitX, exitY, visited);
    }

    private boolean hasPath(int x, int y, int targetX, int targetY, boolean[][] visited) {
        if (x == targetX && y == targetY) return true;
        if (!isValid(x, y) || visited[y][x] || grid[y][x].isWall()) return false;

        visited[y][x] = true;

        // Provo të gjitha drejtimet
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            if (hasPath(newX, newY, targetX, targetY, visited)) return true;
        }

        return false;
    }

    private void placeExit() {
        // Gjej një pozicion të përshtatshëm për daljen në anë të labirintit
        boolean exitPlaced = false;
        while (!exitPlaced) {
            int side = random.nextInt(4); // 0=lart, 1=djathtas, 2=poshtë, 3=majtas

            switch (side) {
                case 0: // lart
                    exitX = 1 + random.nextInt(width-2);
                    exitY = 0;
                    if (!grid[exitY+1][exitX].isWall()) exitPlaced = true;
                    break;
                case 1: // djathtas
                    exitX = width-1;
                    exitY = 1 + random.nextInt(height-2);
                    if (!grid[exitY][exitX-1].isWall()) exitPlaced = true;
                    break;
                case 2: // poshtë
                    exitX = 1 + random.nextInt(width-2);
                    exitY = height-1;
                    if (!grid[exitY-1][exitX].isWall()) exitPlaced = true;
                    break;
                case 3: // majtas
                    exitX = 0;
                    exitY = 1 + random.nextInt(height-2);
                    if (!grid[exitY][exitX+1].isWall()) exitPlaced = true;
                    break;
            }
        }
        grid[exitY][exitX].setType(CellType.EXIT);
    }

    private void placeTreasures() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT treasure_type, points, description FROM treasures"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TreasureType type = TreasureType.valueOf(rs.getString("treasure_type"));
                int points = rs.getInt("points");
                String description = rs.getString("description");

                // Provo të vendosësh thesarin në një pozicion të rastësishëm
                int attempts = 0;
                while (attempts < 100) {
                    int x = random.nextInt(width - 2) + 1;
                    int y = random.nextInt(height - 2) + 1;

                    if (grid[y][x].getType() == CellType.PATH) {
                        Treasure treasure = new Treasure(type, points, description);
                        grid[y][x].setTreasure(treasure);
                        grid[y][x].setType(CellType.TREASURE);
                        break;
                    }
                    attempts++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            placeDefaultTreasures();
        }
    }

    private void placeDefaultTreasures() {
        // Vendos të paktën një nga çdo lloj thesari
        for (TreasureType type : TreasureType.values()) {
            int attempts = 0;
            while (attempts < 100) {
                int x = random.nextInt(width - 2) + 1;
                int y = random.nextInt(height - 2) + 1;

                if (grid[y][x].getType() == CellType.PATH) {
                    Treasure treasure = new Treasure(type, type.getDefaultPoints(),
                            type.getName());
                    grid[y][x].setTreasure(treasure);
                    grid[y][x].setType(CellType.TREASURE);
                    break;
                }
                attempts++;
            }
        }
    }

    private void shuffleArray(int[][] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private boolean isValid(int x, int y) {
        return x > 0 && x < width - 1 && y > 0 && y < height - 1;
    }

    // Getters
    public Cell getCell(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[y][x];
        }
        return null;
    }

    public boolean isWall(int x, int y) {
        Cell cell = getCell(x, y);
        return cell == null || cell.isWall();
    }

    public boolean isTreasure(int x, int y) {
        Cell cell = getCell(x, y);
        return cell != null && cell.isTreasure();
    }

    public boolean isExit(int x, int y) {
        Cell cell = getCell(x, y);
        return cell != null && cell.isExit();
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    public int getExitX() { return exitX; }
    public int getExitY() { return exitY; }

    public String serializeMaze() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = grid[y][x];
                sb.append(cell.getType().ordinal());
                if (cell.isTreasure() && cell.getTreasure() != null) {
                    sb.append(":").append(cell.getTreasure().getType().name())
                            .append(":").append(cell.getTreasure().getPoints());
                }
                sb.append(",");
            }
            sb.append(";");
        }
        return sb.toString();
    }

    public static Maze deserializeMaze(String data) {
        String[] rows = data.split(";");
        int height = rows.length;
        int width = rows[0].split(",").length;

        Maze maze = new Maze(width, height);

        for (int y = 0; y < height; y++) {
            String[] cells = rows[y].split(",");
            for (int x = 0; x < width; x++) {
                String[] cellData = cells[x].split(":");
                int typeOrdinal = Integer.parseInt(cellData[0]);
                CellType type = CellType.values()[typeOrdinal];

                maze.grid[y][x] = new Cell(type);

                if (type == CellType.TREASURE && cellData.length > 2) {
                    TreasureType treasureType = TreasureType.valueOf(cellData[1]);
                    int points = Integer.parseInt(cellData[2]);
                    maze.grid[y][x].setTreasure(new Treasure(treasureType, points,
                            treasureType.getName()));
                }
            }
        }

        return maze;
    }
}
