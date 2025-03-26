package gui;

import model.*;
import controller.GameController;
import javax.swing.*;
import java.awt.*;

public class GameStatusPanel extends JPanel {
    private final JLabel statusLabel;
    private final GameController gameController;

    public GameStatusPanel(GameController controller) {
        this.gameController = controller;
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));

        // Krijo dhe stilo label-in
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Shto border për pamje më të mirë
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Shto label në panel
        add(statusLabel);

        // Bëj update fillestar
        update();
    }

    public void update() {
        Player player = gameController.getPlayer();
        String text = String.format(
                "Niveli: %d | Pikët aktuale: %d | Pikët totale: %d | Thesare: %d",
                gameController.getCurrentLevel(),
                player.getCurrentLevelScore(),
                player.getTotalScore(),
                player.getCollectedTreasures()
        );
        statusLabel.setText(text);
    }
}