package gui;

import model.*;
import controller.GameController;
import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {
    private static final int CELL_SIZE = 35;
    private final GameController gameController;

    // Ngjyrat
    private final Color wallColor = new Color(48, 48, 48);
    private final Color pathColor = new Color(255, 255, 255);
    private final Color treasureColor = new Color(255, 215, 0);
    private final Color exitColor = new Color(0, 255, 0);
    private final Color playerColor = new Color(255, 0, 0);
    private final Color startColor = new Color(0, 191, 255);

    public MazePanel(GameController gameController) {
        this.gameController = gameController;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setPreferredSize(new Dimension(15 * CELL_SIZE, 15 * CELL_SIZE));
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        Maze maze = gameController.getMaze();
        Player player = gameController.getPlayer();

        // Vizato labirintin
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                Cell cell = maze.getCell(x, y);
                if (cell != null) {
                    int px = x * CELL_SIZE;
                    int py = y * CELL_SIZE;

                    // Ngjyros qelizën bazë
                    switch (cell.getType()) {
                        case WALL:
                            g2d.setColor(wallColor);
                            g2d.fillRect(px, py, CELL_SIZE, CELL_SIZE);
                            break;
                        case PATH:
                            g2d.setColor(pathColor);
                            g2d.fillRect(px, py, CELL_SIZE, CELL_SIZE);
                            break;
                        case TREASURE:
                            g2d.setColor(pathColor);
                            g2d.fillRect(px, py, CELL_SIZE, CELL_SIZE);
                            if (cell.getTreasure() != null) {
                                drawTreasure(g2d, px, py, cell.getTreasure().getType());
                            }
                            break;
                        case EXIT:
                            g2d.setColor(exitColor);
                            g2d.fillRect(px, py, CELL_SIZE, CELL_SIZE);
                            drawExit(g2d, px, py);
                            break;
                        case START:
                            g2d.setColor(startColor);
                            g2d.fillRect(px, py, CELL_SIZE, CELL_SIZE);
                            break;
                    }

                    // Vizato kornizën e qelizës
                    g2d.setColor(Color.GRAY);
                    g2d.drawRect(px, py, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        // Vizato lojtarin
        int playerX = player.getX() * CELL_SIZE;
        int playerY = player.getY() * CELL_SIZE;
        drawPlayer(g2d, playerX, playerY);
    }

    private void drawTreasure(Graphics2D g2d, int x, int y, TreasureType type) {
        switch (type) {
            case COIN:
                drawCoin(g2d, x, y);
                break;
            case GEM:
                drawGem(g2d, x, y);
                break;
            case CHEST:
                drawChest(g2d, x, y);
                break;
            case CROWN:
                drawCrown(g2d, x, y);
                break;
        }
    }

    private void drawCoin(Graphics2D g2d, int x, int y) {
        int padding = CELL_SIZE / 4;
        // Rreth i jashtëm
        g2d.setColor(new Color(255, 215, 0)); // Gold
        g2d.fillOval(x + padding, y + padding,
                CELL_SIZE - 2*padding, CELL_SIZE - 2*padding);
        // Konturi
        g2d.setColor(new Color(218, 165, 32)); // Dark gold
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x + padding, y + padding,
                CELL_SIZE - 2*padding, CELL_SIZE - 2*padding);
        // Simboli C
        g2d.setColor(new Color(184, 134, 11)); // Darker gold
        g2d.setFont(new Font("Arial", Font.BOLD, CELL_SIZE/3));
        FontMetrics fm = g2d.getFontMetrics();
        String symbol = "C";
        int textX = x + (CELL_SIZE - fm.stringWidth(symbol)) / 2;
        int textY = y + (CELL_SIZE + fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(symbol, textX, textY);
    }

    private void drawGem(Graphics2D g2d, int x, int y) {
        int padding = CELL_SIZE / 6;
        int[] xPoints = {
                x + CELL_SIZE/2,
                x + CELL_SIZE - padding,
                x + CELL_SIZE - padding,
                x + CELL_SIZE/2,
                x + padding,
                x + padding
        };
        int[] yPoints = {
                y + padding,
                y + CELL_SIZE/3,
                y + 2*CELL_SIZE/3,
                y + CELL_SIZE - padding,
                y + 2*CELL_SIZE/3,
                y + CELL_SIZE/3
        };

        // Fill
        g2d.setColor(new Color(0, 191, 255)); // Light blue
        g2d.fillPolygon(xPoints, yPoints, 6);

        // Outline
        g2d.setColor(new Color(135, 206, 250)); // Sky blue
        g2d.setStroke(new BasicStroke(2));
        g2d.drawPolygon(xPoints, yPoints, 6);

        // Highlight
        g2d.setColor(new Color(255, 255, 255, 100));
        int[] hlXPoints = {
                x + padding,
                x + CELL_SIZE/2,
                x + CELL_SIZE - padding
        };
        int[] hlYPoints = {
                y + CELL_SIZE/3,
                y + padding,
                y + CELL_SIZE/3
        };
        g2d.fillPolygon(hlXPoints, hlYPoints, 3);
    }

    private void drawChest(Graphics2D g2d, int x, int y) {
        int padding = CELL_SIZE / 6;

        // Baza e arkës
        g2d.setColor(new Color(139, 69, 19)); // Brown
        g2d.fillRect(x + padding, y + CELL_SIZE/3,
                CELL_SIZE - 2*padding, CELL_SIZE/2);

        // Kapaku i arkës
        g2d.fillArc(x + padding, y + CELL_SIZE/4,
                CELL_SIZE - 2*padding, CELL_SIZE/3,
                0, -180);

        // Dryna
        g2d.setColor(new Color(255, 215, 0)); // Gold
        g2d.fillRect(x + CELL_SIZE/2 - 2, y + CELL_SIZE/3,
                4, CELL_SIZE/6);

        // Detajet
        g2d.setColor(new Color(101, 67, 33)); // Dark brown
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(x + padding, y + CELL_SIZE/2,
                x + CELL_SIZE - padding, y + CELL_SIZE/2);
    }

    private void drawCrown(Graphics2D g2d, int x, int y) {
        int padding = CELL_SIZE / 6;

        // Baza e kurorës
        int[] xPoints = {
                x + padding,
                x + CELL_SIZE/4,
                x + CELL_SIZE/2,
                x + 3*CELL_SIZE/4,
                x + CELL_SIZE - padding
        };
        int[] yPoints = {
                y + CELL_SIZE - padding*2,
                y + padding,
                y + CELL_SIZE/3,
                y + padding,
                y + CELL_SIZE - padding*2
        };

        // Fill
        g2d.setColor(new Color(255, 215, 0)); // Gold
        g2d.fillPolygon(xPoints, yPoints, 5);

        // Base
        g2d.fillRect(x + padding, y + CELL_SIZE - padding*2,
                CELL_SIZE - 2*padding, padding);

        // Gurët e çmuar
        g2d.setColor(new Color(255, 0, 0)); // Ruby
        g2d.fillOval(x + CELL_SIZE/2 - 3, y + CELL_SIZE/3, 6, 6);
        g2d.setColor(new Color(0, 0, 255)); // Sapphire
        g2d.fillOval(x + CELL_SIZE/4 - 2, y + padding + 5, 4, 4);
        g2d.fillOval(x + 3*CELL_SIZE/4 - 2, y + padding + 5, 4, 4);
    }

    private void drawPlayer(Graphics2D g2d, int x, int y) {
        g2d.setColor(playerColor);
        int padding = CELL_SIZE / 4;
        g2d.fillOval(x + padding, y + padding,
                CELL_SIZE - 2*padding, CELL_SIZE - 2*padding);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x + padding, y + padding,
                CELL_SIZE - 2*padding, CELL_SIZE - 2*padding);
    }

    private void drawExit(Graphics2D g2d, int x, int y) {
        int padding = CELL_SIZE / 6;

        // Vizato portën
        g2d.setColor(new Color(34, 139, 34)); // Dark green
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x + padding, y + padding,
                CELL_SIZE - 2*padding, CELL_SIZE - 2*padding);

        // Doreza e portës
        g2d.fillOval(x + CELL_SIZE - 2*padding, y + CELL_SIZE/2 - padding/2,
                padding, padding);
    }
}
