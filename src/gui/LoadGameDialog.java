package gui;

import controller.GameController;
import util.DatabaseConfig;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadGameDialog extends JDialog {
    private final GameController gameController;
    private JList<SavedGameInfo> savesList;
    private DefaultListModel<SavedGameInfo> listModel;
    
    public LoadGameDialog(JFrame parent, GameController controller) {
        super(parent, "Ngarko Lojën", true);
        this.gameController = controller;
        
        setLayout(new BorderLayout(10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        // Krijo listën e lojërave të ruajtura
        listModel = new DefaultListModel<>();
        savesList = new JList<>(listModel);
        savesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Shto scroll pane
        JScrollPane scrollPane = new JScrollPane(savesList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel për butonat
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loadButton = new JButton("Ngarko");
        JButton cancelButton = new JButton("Anulo");
        
        loadButton.addActionListener(e -> handleLoad());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(loadButton);
        buttonPanel.add(cancelButton);
        
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Ngarko lojërat e ruajtura
        loadSavedGames();
    }
    
    private void loadSavedGames() {
        listModel.clear();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT game_id, user_id, saved_at, score FROM saved_games ORDER BY saved_at DESC";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    SavedGameInfo gameInfo = new SavedGameInfo(
                        rs.getInt("game_id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("saved_at"),
                        rs.getInt("score")
                    );
                    listModel.addElement(gameInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Ndodhi një gabim gjatë ngarkimit të lojërave të ruajtura",
                "Gabim",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleLoad() {
        SavedGameInfo selected = savesList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Ju lutem zgjidhni një lojë për të ngarkuar",
                "Gabim",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (gameController.loadGame(selected.getGameId())) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Ndodhi një gabim gjatë ngarkimit të lojës",
                "Gabim",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
