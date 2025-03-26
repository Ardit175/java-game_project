package main;

import gui.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class MazeGame {
    public static void main(String[] args) {
        // Vendos Look and Feel të sistemit
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Vendos parametrat e shkrimit
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Nise aplikacionin në EDT
        SwingUtilities.invokeLater(() -> {
            try {
                // Testo lidhjen me bazën e të dhënave
                testDatabaseConnection();

                // Krijo dhe shfaq login frame
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);

            } catch (Exception e) {
                showErrorDialog(e);
                System.exit(1);
            }
        });
    }

    private static void testDatabaseConnection() {
        try {
            util.DatabaseConfig.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("Nuk mund të lidhet me bazën e të dhënave", e);
        }
    }

    private static void showErrorDialog(Exception e) {
        String errorMessage = String.format(
                "Ndodhi një gabim gjatë nisjes së aplikacionit:\n\n%s\n\n" +
                        "Detaje teknike:\n%s",
                e.getMessage(),
                e.getCause() != null ? e.getCause().getMessage() : "N/A"
        );

        JOptionPane.showMessageDialog(
                null,
                errorMessage,
                "Gabim në Nisje",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
