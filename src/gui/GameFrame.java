package gui;

import controller.GameStatus;
import model.*;
import controller.GameController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame {
    private final GameController gameController;
    private final MazePanel mazePanel;
    private final GameStatusPanel statusPanel;
    private final ScoreInfoPanel scoreInfoPanel;
    private final Timer gameTimer;
    private final String username;

    public GameFrame(String username) {
        this.username = username;
        this.gameController = new GameController();

        setTitle("Maze Game - " + username);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 800);
        setResizable(false);
        setLocationRelativeTo(null);

        // Inicializojmë komponentët
        initializeGame();

        // Krijojmë panelet
        mazePanel = new MazePanel(gameController);
        statusPanel = new GameStatusPanel(gameController);
        scoreInfoPanel = new ScoreInfoPanel();

        // Krijojmë layout-in
        createLayout();

        // Setup kontrollet
        setupKeyBindings();

        // Timer për update
        gameTimer = new Timer(100, e -> updateGame());
        gameTimer.start();

        // Window listener për mbylljen
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleExit();
            }
        });
    }

    private void createLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel qendror që mban labirintin
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(statusPanel, BorderLayout.NORTH);
        centerPanel.add(mazePanel, BorderLayout.CENTER);

        // Panel për butonat në të majtë
        JPanel leftControlPanel = createMovementButtonPanel();

        // Panel për kontrollet dhe informacionin në të djathtë
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(createGameControlPanel(), BorderLayout.NORTH);
        rightPanel.add(scoreInfoPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(leftControlPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    private JPanel createMovementButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Kontrollet"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Butonat e lëvizjes
        JButton upButton = new JButton("▲");
        JButton downButton = new JButton("▼");
        JButton leftButton = new JButton("◄");
        JButton rightButton = new JButton("►");

        // Madhësia e butonave
        Dimension buttonSize = new Dimension(60, 60);
        upButton.setPreferredSize(buttonSize);
        downButton.setPreferredSize(buttonSize);
        leftButton.setPreferredSize(buttonSize);
        rightButton.setPreferredSize(buttonSize);

        // Vendosja e butonave
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(upButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(leftButton, gbc);

        gbc.gridx = 2;
        panel.add(rightButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(downButton, gbc);

        // Event listeners
        upButton.addActionListener(e -> handleMove(Direction.UP));
        downButton.addActionListener(e -> handleMove(Direction.DOWN));
        leftButton.addActionListener(e -> handleMove(Direction.LEFT));
        rightButton.addActionListener(e -> handleMove(Direction.RIGHT));

        return panel;
    }

    private JPanel createGameControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Loja"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Butonat e lojës
        JButton newGameButton = new JButton("Lojë e Re");
        JButton saveButton = new JButton("Ruaj");
        JButton loadButton = new JButton("Ngarko");

        // Butonat e sistemit
        JButton logoutButton = new JButton("Dil nga llogaria");
        JButton exitButton = new JButton("Mbyll lojën");

        // Shtimi i butonave
        gbc.gridy = 0;
        panel.add(newGameButton, gbc);
        gbc.gridy = 1;
        panel.add(saveButton, gbc);
        gbc.gridy = 2;
        panel.add(loadButton, gbc);

        // Separator
        gbc.gridy = 3;
        gbc.insets = new Insets(15, 5, 15, 5);
        panel.add(new JSeparator(), gbc);

        // Butonat e sistemit
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 4;
        panel.add(logoutButton, gbc);
        gbc.gridy = 5;
        panel.add(exitButton, gbc);

        // Event listeners
        newGameButton.addActionListener(e -> handleNewGame());
        saveButton.addActionListener(e -> handleSaveGame());
        loadButton.addActionListener(e -> handleLoadGame());
        logoutButton.addActionListener(e -> handleLogout());
        exitButton.addActionListener(e -> handleExit());

        return panel;
    }

    private void setupKeyBindings() {
        InputMap inputMap = mazePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = mazePanel.getActionMap();

        // Lëvizjet me tastet shigjetë
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");

        actionMap.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMove(Direction.UP);
            }
        });
        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMove(Direction.DOWN);
            }
        });
        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMove(Direction.LEFT);
            }
        });
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMove(Direction.RIGHT);
            }
        });

        // Shortcuts
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "save");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK), "load");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "new");

        actionMap.put("save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSaveGame();
            }
        });
        actionMap.put("load", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLoadGame();
            }
        });
        actionMap.put("new", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleNewGame();
            }
        });
    }

    private void handleMove(Direction direction) {
        boolean validMove = gameController.movePlayer(direction);

        // Përditësojmë pamjen
        mazePanel.repaint();
        statusPanel.update();

        // Kontrollojmë për rezultatin e lëvizjes
        if (!validMove) {  // Kjo ndodh kur përplasemi me mur
            String message = gameController.getLastMoveResult();
            if (message != null) {
                showGameResult(message);
            }
            return;
        }

        // Kontrollojmë nëse kemi fituar nivelin
        String moveResult = gameController.getLastMoveResult();
        if (moveResult != null) {
            showGameResult(moveResult);
        }
    }

    private void showGameResult(String message) {
        GameStatus status = gameController.getStatus();

        if (status == GameStatus.WON) {
            int response = JOptionPane.showConfirmDialog(this,
                    message,
                    "Niveli u kalua!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                gameController.startNextLevel();
                mazePanel.repaint();
                statusPanel.update();
                scoreInfoPanel.updateLeaderboard();
            } else {
                gameController.saveHighScore();
                handleNewGame();
            }
        } else if (status == GameStatus.LOST) {
            gameController.saveHighScore();
            JOptionPane.showMessageDialog(this,
                    message,
                    "Game Over!",
                    JOptionPane.INFORMATION_MESSAGE);
            scoreInfoPanel.updateLeaderboard();
            handleNewGame();
        }
    }


    private void initializeGame() {
        gameController.startNewGame(username);
    }

    public void handleNewGame() {
        if (gameController.hasUnsavedChanges() && gameController.isGameActive()) {
            int response = JOptionPane.showConfirmDialog(this,
                    "Keni ndryshime të paruajtura. Doni të ruani lojën para se të filloni një lojë të re?",
                    "Ruaj Ndryshimet?",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                if (!handleSaveGame()) {
                    return;
                }
            } else if (response == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        gameController.startNewGame(username);
        mazePanel.repaint();
        statusPanel.update();
        scoreInfoPanel.updateLeaderboard();
        mazePanel.requestFocusInWindow();
    }

    public boolean handleSaveGame() {
        if (gameController.saveGame()) {
            JOptionPane.showMessageDialog(this,
                    "Loja u ruajt me sukses!\nID e lojës: " + gameController.getGameId(),
                    "Ruajtja me Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(this,
                    "Ndodhi një gabim gjatë ruajtjes së lojës.",
                    "Gabim në Ruajtje",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void handleLoadGame() {
        if (gameController.hasUnsavedChanges() && gameController.isGameActive()) {
            int response = JOptionPane.showConfirmDialog(this,
                    "Keni ndryshime të paruajtura. Doni të ruani lojën aktuale?",
                    "Ruaj Ndryshimet?",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                if (!handleSaveGame()) {
                    return;
                }
            } else if (response == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        String input = JOptionPane.showInputDialog(this,
                "Vendosni ID e lojës për të ngarkuar:",
                "Ngarko Lojën",
                JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.isEmpty()) {
            try {
                int gameId = Integer.parseInt(input);
                if (gameController.loadGame(gameId)) {
                    mazePanel.repaint();
                    statusPanel.update();
                    scoreInfoPanel.updateLeaderboard();
                    mazePanel.requestFocusInWindow();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Nuk u gjet lojë me ID: " + gameId,
                            "Gabim në Ngarkim",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "ID e lojës duhet të jetë numër.",
                        "ID e Pavlefshme",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleLogout() {
        if (gameController.hasUnsavedChanges() && gameController.isGameActive()) {
            int response = JOptionPane.showConfirmDialog(this,
                    "Keni ndryshime të paruajtura. Doni të ruani lojën para se të dilni nga llogaria?",
                    "Ruaj para daljes?",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                if (!handleSaveGame()) {
                    return;
                }
            } else if (response == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        dispose();
        new LoginFrame().setVisible(true);
    }

    private void handleExit() {
        if (gameController.hasUnsavedChanges() && gameController.isGameActive()) {
            int saveResponse = JOptionPane.showConfirmDialog(this,
                    "Keni ndryshime të paruajtura. Doni të ruani lojën para se të mbyllni?",
                    "Ruaj para mbylljes?",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (saveResponse == JOptionPane.YES_OPTION) {
                if (!handleSaveGame()) {
                    return;
                }
            } else if (saveResponse == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        int exitResponse = JOptionPane.showConfirmDialog(this,
                "Jeni të sigurt që doni të mbyllni lojën?",
                "Konfirmo mbylljen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (exitResponse == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    private void updateGame() {
        if (gameController.isGameActive()) {
            mazePanel.repaint();
            statusPanel.update();
        }
    }
}