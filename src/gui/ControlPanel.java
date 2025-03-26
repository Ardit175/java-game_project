package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ControlPanel extends JPanel {
    private final GameFrame parentFrame;
    
    public ControlPanel(GameFrame parent) {
        this.parentFrame = parent;
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Krijo butonat
        JButton newGameButton = createStyledButton("Lojë e Re", e -> parentFrame.handleNewGame());
        JButton saveButton = createStyledButton("Ruaj", e -> parentFrame.handleSaveGame());
        JButton loadButton = createStyledButton("Ngarko", e -> parentFrame.handleLoadGame());
        JButton helpButton = createStyledButton("Ndihmë", e -> showHelp());
        
        // Shto ikonat në butona (mund të shtohen ikonat aktuale më vonë)
        newGameButton.setIcon(createIcon("new"));
        saveButton.setIcon(createIcon("save"));
        loadButton.setIcon(createIcon("load"));
        helpButton.setIcon(createIcon("help"));
        
        // Shto butonat në panel
        add(newGameButton);
        add(Box.createHorizontalStrut(5));
        add(saveButton);
        add(Box.createHorizontalStrut(5));
        add(loadButton);
        add(Box.createHorizontalStrut(20));  // Hapësirë më e madhe para butonit të ndihmës
        add(helpButton);
    }
    
    private JButton createStyledButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Efektet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 240, 240));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(UIManager.getColor("Button.background"));
            }
        });
        
        return button;
    }
    
    private Icon createIcon(String type) {
        // Këtu mund të krijoni ikona të thjeshta ose të ngarkoni nga resources
        int size = 16;
        ImageIcon icon = new ImageIcon(new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB));
        Graphics2D g2d = (Graphics2D) icon.getImage().getGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        switch (type) {
            case "new":
                g2d.setColor(new Color(0, 150, 0));
                g2d.fillRect(3, 3, 10, 10);
                break;
            case "save":
                g2d.setColor(new Color(0, 100, 150));
                g2d.fillRect(3, 3, 10, 10);
                g2d.setColor(Color.WHITE);
                g2d.fillRect(5, 5, 6, 6);
                break;
            case "load":
                g2d.setColor(new Color(150, 100, 0));
                g2d.fillRect(3, 3, 10, 10);
                g2d.setColor(Color.WHITE);
                g2d.drawLine(5, 8, 11, 8);
                break;
            case "help":
                g2d.setColor(new Color(100, 100, 100));
                g2d.fillOval(3, 3, 10, 10);
                g2d.setColor(Color.WHITE);
                g2d.drawString("?", 6, 11);
                break;
        }
        
        g2d.dispose();
        return icon;
    }
    
    private void showHelp() {
        String helpText = 
            "Kontrollet e lojës:\n\n" +
            "→ Lëviz djathtas\n" +
            "← Lëviz majtas\n" +
            "↑ Lëviz lart\n" +
            "↓ Lëviz poshtë\n\n" +
            "CTRL+S: Ruaj lojën\n" +
            "CTRL+L: Ngarko lojën\n" +
            "CTRL+N: Lojë e re";
            
        JOptionPane.showMessageDialog(
            this,
            helpText,
            "Kontrollet e Lojës",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
