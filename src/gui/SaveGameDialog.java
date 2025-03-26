package gui;

import javax.swing.*;
import java.awt.*;
import controller.GameController;
import java.util.List;
import java.sql.*;
import util.DatabaseConfig;

public class SaveGameDialog extends JDialog {
    private final GameController gameController;
    private JTextField nameField;
    
    public SaveGameDialog(JFrame parent, GameController controller) {
        super(parent, "Ruaj Lojën", true);
        this.gameController = controller;
        
        setLayout(new BorderLayout(10, 10));
        setSize(300, 150);
        setLocationRelativeTo(parent);
        
        // Panel për inputin
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Emri i ruajtjes:"));
        nameField = new JTextField(20);
        inputPanel.add(nameField);
        
        // Panel për butonat
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Ruaj");
        JButton cancelButton = new JButton("Anulo");
        
        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void handleSave() {
        String saveName = nameField.getText().trim();
        if (saveName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ju lutem vendosni një emër për ruajtjen",
                "Gabim",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (gameController.saveGame()) {
            JOptionPane.showMessageDialog(this,
                "Loja u ruajt me sukses!\nID e lojës: " + gameController.getGameId(),
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Ndodhi një gabim gjatë ruajtjes së lojës",
                "Gabim",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

class SavedGameInfo {
    private final int gameId;
    private final int userId;
    private final Timestamp savedAt;
    private final int score;
    
    public SavedGameInfo(int gameId, int userId, Timestamp savedAt, int score) {
        this.gameId = gameId;
        this.userId = userId;
        this.savedAt = savedAt;
        this.score = score;
    }
    
    public int getGameId() { return gameId; }
    
    @Override
    public String toString() {
        return String.format("ID: %d | Data: %s | Pikët: %d",
            gameId, savedAt.toString(), score);
    }
}
