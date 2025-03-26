package gui;

import javax.swing.*;
import java.awt.event.*;
import controller.GameController;

public class GameMenuBar extends JMenuBar {
    private final GameFrame parentFrame;
    private final GameController gameController;
    
    public GameMenuBar(GameFrame parent, GameController controller) {
        this.parentFrame = parent;
        this.gameController = controller;
        initializeMenus();
    }
    
    private void initializeMenus() {
        // Menu Loja
        JMenu gameMenu = new JMenu("Loja");
        gameMenu.setMnemonic(KeyEvent.VK_L);
        
        // Opsionet e menu-së Loja
        JMenuItem newGame = new JMenuItem("Lojë e Re", KeyEvent.VK_N);
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newGame.addActionListener(e -> parentFrame.handleNewGame());
        
        JMenuItem saveGame = new JMenuItem("Ruaj", KeyEvent.VK_R);
        saveGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveGame.addActionListener(e -> parentFrame.handleSaveGame());
        
        JMenuItem loadGame = new JMenuItem("Ngarko", KeyEvent.VK_N);
        loadGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        loadGame.addActionListener(e -> parentFrame.handleLoadGame());
        
        JMenuItem exit = new JMenuItem("Dil", KeyEvent.VK_D);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        exit.addActionListener(e -> handleExit());
        
        gameMenu.add(newGame);
        gameMenu.addSeparator();
        gameMenu.add(saveGame);
        gameMenu.add(loadGame);
        gameMenu.addSeparator();
        gameMenu.add(exit);
        
        // Menu Ndihmë
        JMenu helpMenu = new JMenu("Ndihmë");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem howToPlay = new JMenuItem("Si të luash", KeyEvent.VK_S);
        howToPlay.addActionListener(e -> showHowToPlay());
        
        JMenuItem about = new JMenuItem("Rreth lojës", KeyEvent.VK_R);
        about.addActionListener(e -> showAbout());
        
        helpMenu.add(howToPlay);
        helpMenu.addSeparator();
        helpMenu.add(about);
        
        // Shto menutë në menu bar
        add(gameMenu);
        add(helpMenu);
    }
    
    private void handleExit() {
        int response = JOptionPane.showConfirmDialog(
            parentFrame,
            "Jeni të sigurt që doni të dilni?",
            "Konfirmo Daljen",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private void showHowToPlay() {
        String instructions = 
            "Si të luash Maze Game:\n\n" +
            "1. Lëvizja:\n" +
            "   - Përdor tastet me shigjeta për të lëvizur\n" +
            "   - ↑ (Lart), ↓ (Poshtë), ← (Majtas), → (Djathtas)\n\n" +
            "2. Objektivi:\n" +
            "   - Mblidh sa më shumë thesare që mundesh\n" +
            "   - Arrij te dalja (porta jeshile)\n" +
            "   - Shmang muret\n\n" +
            "3. Pikët:\n" +
            "   - Çdo thesar jep pikë të ndryshme\n" +
            "   - Fito duke mbledhur sa më shumë pikë\n\n" +
            "4. Ruajtja:\n" +
            "   - Mund të ruash lojën në çdo moment\n" +
            "   - Përdor ID e lojës për ta ngarkuar më vonë";
        
        JOptionPane.showMessageDialog(
            parentFrame,
            instructions,
            "Si të luash",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void showAbout() {
        String about = 
            "Maze Game v1.0\n\n" +
            "Një lojë labirinti e krijuar si detyrë kursi për\n" +
            "lëndën Programim i Orientuar nga Objektet.\n\n" +
            "© 2024 Të gjitha të drejtat të rezervuara.";
        
        JOptionPane.showMessageDialog(
            parentFrame,
            about,
            "Rreth lojës",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
