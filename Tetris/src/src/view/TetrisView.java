package src.view;

import javax.swing.*;
import java.awt.*;
import src.controller.TetrisController;
import src.model.HighScores;
import src.model.TetrisBoard;
import src.model.TetrisGame;
import src.model.Tetromino;

public class TetrisView extends JPanel {
    private final TetrisGame game;
    private Tetromino tetromino;
    private final HighScores highScores;
    private final AboutView aboutView;
    private final HighScoresView highScoresView;

    public TetrisView(TetrisGame game, HighScores highScores) {
        this.game = game;
        this.highScores = highScores;
        this.tetromino = game.getBoard().getCurrentPiece();
        this.aboutView = new AboutView();
        this.highScoresView = new HighScoresView(highScores);
        System.out.println("TetrisView initialized.");
        initializeGUI();
    }

    private void initializeGUI() {
        setPreferredSize(new Dimension(500, 600));
        JButton aboutButton = new JButton("About");
        JButton highScoresButton = new JButton("High Scores");

        aboutButton.addActionListener(e -> {
            System.out.println("About button clicked.");
            showAbout();
        });
        highScoresButton.addActionListener(e -> {
            System.out.println("High Scores button clicked.");
            showHighScores();
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(aboutButton);
        controlPanel.add(highScoresButton);
        

        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        System.out.println("GUI initialized.");
    }

    private void showAbout() {
        System.out.println("Displaying About view.");
        JOptionPane.showMessageDialog(null, aboutView, "About Tetris", JOptionPane.INFORMATION_MESSAGE);
        requestFocusInWindow();
    }

    private void showHighScores() {
        System.out.println("Displaying High Scores view.");
        highScoresView.updateScores();
        JOptionPane.showMessageDialog(null, highScoresView, "High Scores", JOptionPane.INFORMATION_MESSAGE);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("Painting component.");
        drawBoard(g);
        drawTetrominos(g);
        drawHighScores(g);
        drawNextTetromino(g);
        drawScore(g);
    }
    
    private void drawNextTetromino(Graphics g) {
        System.out.println("Drawing next tetromino.");
        Tetromino nextTetromino = game.getNextTetromino();
        int offsetX = 2; // смещение по оси X для отображения следующей фигуры
        int offsetY = 7; // смещение по оси Y для отображения следующей фигуры
        for (Point point : nextTetromino.getBlocks()) {
            int x = point.x - nextTetromino.getX() + offsetX;
            int y = point.y - nextTetromino.getY() + offsetY;
            g.setColor(nextTetromino.getColor());
            g.fillRect(x * (TetrisBoard.BLOCK_SIZE / 3), y * (TetrisBoard.BLOCK_SIZE / 3), (TetrisBoard.BLOCK_SIZE / 3), (TetrisBoard.BLOCK_SIZE / 3));
        }
    }

    private void drawBoard(Graphics g) {
        System.out.println("Drawing board.");
        int[][] grid = game.getBoard().getGrid();
        int cellSize = 30;
        for (int i = 0; i < TetrisBoard.ROW_COUNT; i++) {
            for (int j = 0; j < TetrisBoard.COL_COUNT; j++) {
                switch (grid[i][j]) {
                    case 0 -> {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    case 2 -> {
                        g.setColor(Color.CYAN);
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    case 3 -> {
                        g.setColor(Color.BLUE);
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    case 4 -> {
                        g.setColor(Color.ORANGE);
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    case 5 -> {
                        g.setColor(Color.YELLOW);
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    case 6 -> {
                        g.setColor(Color.GREEN);
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    case 7 -> {
                        g.setColor(Color.MAGENTA);
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    case 8 -> {
                        g.setColor(Color.RED);
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    default -> {
                    }
                }
            }
        }
    }

    private void drawTetrominos(Graphics g) {
        System.out.println("Drawing tetrominos.");
        this.tetromino = game.getBoard().getCurrentPiece();
        for (Point point : game.getBoard().getCurrentPiece().getBlocks()) {
            int x = point.x;
            int y = point.y;
            /*g.setColor(Color.BLACK);
            g.drawRect(x * TetrisBoard.BLOCK_SIZE, y * TetrisBoard.BLOCK_SIZE, TetrisBoard.BLOCK_SIZE, TetrisBoard.BLOCK_SIZE);*/
            g.setColor(tetromino.getColor());
            g.fillRect(x * TetrisBoard.BLOCK_SIZE, y * TetrisBoard.BLOCK_SIZE, TetrisBoard.BLOCK_SIZE, TetrisBoard.BLOCK_SIZE);
        }
    }

    private void drawHighScores(Graphics g) {
        System.out.println("Drawing high scores.");
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("High Scores", 350, 50);
        int y = 100;
        for (HighScores.ScoreEntry score : highScores.getScores()) {
            g.drawString(score.getPlayerName() + ": " + score.getScore(), 350, y);
            y += 30;
        }
    }

    public void addController(TetrisController.TetrisControlListener listener) {
        addKeyListener(listener);
        setFocusable(true);
        System.out.println("Controller added.");
    }

    public void updateView() {
        System.out.println("View updated.");
        repaint();
    }

    private void drawScore(Graphics g) {
        System.out.println("Drawing score.");
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        int score = game.getScore(); // предполагаем, что у вас есть метод getScore() в классе TetrisGame
        g.drawString("Score: " + score, 10, 50);
    }
    
    public void showGameOverDialog() {
        System.out.println("Displaying game over dialog.");
        String playerName = JOptionPane.showInputDialog(null, "Game Over! Enter your name:", "Game Over", JOptionPane.PLAIN_MESSAGE);
        if (playerName != null && !playerName.trim().isEmpty()) {
            highScores.addScore(playerName, game.getScore());
        }
        int option = JOptionPane.showOptionDialog(null, "Play again?", "Game Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option == JOptionPane.YES_OPTION) {
            game.startGame();
            requestFocusInWindow();
        } else {
            game.exitGame();
        }
    }
}
