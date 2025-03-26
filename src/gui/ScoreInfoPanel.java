package gui;

import util.DatabaseConfig;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class ScoreInfoPanel extends JPanel {
    private final JLabel treasureInfoLabel;
    private final JPanel leaderboardPanel;

    public ScoreInfoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Informacioni i Lojës"));

        // Panel për informacionin e thesareve
        JPanel treasurePanel = new JPanel();
        treasurePanel.setLayout(new BoxLayout(treasurePanel, BoxLayout.Y_AXIS));
        treasurePanel.setBorder(BorderFactory.createTitledBorder("Thesaret dhe Pikët"));

        treasureInfoLabel = new JLabel("<html>" +
                "🟡 Monedhë Ari: 10 pikë<br>" +
                "💎 Gur i Çmuar: 50 pikë<br>" +
                "📦 Arkë Thesari: 100 pikë<br>" +
                "👑 Kurorë Mbretërore: 200 pikë" +
                "</html>");
        treasurePanel.add(treasureInfoLabel);

        // Panel për leaderboard
        leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBorder(BorderFactory.createTitledBorder("Top Lojtarët"));
        updateLeaderboard();

        add(treasurePanel, BorderLayout.NORTH);
        add(leaderboardPanel, BorderLayout.CENTER);
    }

    public void updateLeaderboard() {
        leaderboardPanel.removeAll();

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT u.username, h.score, h.date_achieved " +
                    "FROM high_scores h " +
                    "JOIN users u ON h.user_id = u.user_id " +
                    "ORDER BY h.score DESC " +
                    "LIMIT 5";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                boolean hasScores = false;

                while (rs.next()) {
                    hasScores = true;
                    String username = rs.getString("username");
                    int score = rs.getInt("score");
                    Timestamp date = rs.getTimestamp("date_achieved");

                    JLabel scoreLabel = new JLabel(String.format(
                            "<html>%s - %d pikë<br><small>%s</small></html>",
                            username, score,
                            new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date)
                    ));

                    scoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
                    leaderboardPanel.add(scoreLabel);
                }

                if (!hasScores) {
                    leaderboardPanel.add(new JLabel("Nuk ka ende rezultate"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            leaderboardPanel.add(new JLabel("Gabim në ngarkim të rezultateve"));
        }

        leaderboardPanel.revalidate();
        leaderboardPanel.repaint();
    }

    private java.util.List<ScoreEntry> getTopScores() {
        java.util.List<ScoreEntry> scores = new java.util.ArrayList<>();
        try (java.sql.Connection conn = util.DatabaseConfig.getConnection()) {
            String sql = "SELECT username, MAX(score) as max_score " +
                    "FROM saved_games s JOIN users u ON s.user_id = u.user_id " +
                    "WHERE game_status = 'WON' " +
                    "GROUP BY username " +
                    "ORDER BY max_score DESC LIMIT 5";

            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
                java.sql.ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    scores.add(new ScoreEntry(
                            rs.getString("username"),
                            rs.getInt("max_score")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scores;
    }

    private static class ScoreEntry {
        private final String username;
        private final int score;

        public ScoreEntry(String username, int score) {
            this.username = username;
            this.score = score;
        }

        public String getUsername() { return username; }
        public int getScore() { return score; }
    }
}
