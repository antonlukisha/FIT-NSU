package src.model;

import java.util.ArrayList;

public class TetrisGame {
    private final TetrisBoard board;
    private final ArrayList<Tetromino> tetrominos;
    private int score;
    private boolean isGameOver;
    private boolean isPaused;

    public TetrisGame() {
        board = new TetrisBoard();
        tetrominos = new ArrayList<>();
        tetrominos.add(board.getCurrentPiece());
        score = 0;
        isGameOver = false;
        System.out.println("Tetris game initialized.");
    }

    public void update() {
        if (!isGameOver && !isPaused) {
            board.update();
            int linesCleared = board.getNumFilledRows();
            updateScore(linesCleared);
            board.setNullNumFilledRows();
            System.out.println("Game updated. Score: " + score);
            if (board.isGameOver()) {
                isGameOver = true;
                System.out.println("Game over.");
            }
            if (tetrominos.getLast() != board.getCurrentPiece()) {
                tetrominos.add(board.getCurrentPiece());
            }
        }
    }
    
    public void updateScore(int linesCleared) {
        switch (linesCleared) {
            case 1 -> score += 100;
            case 2 -> score += 300;
            case 3 -> score += 500;
            case 4 -> score += 800;
        }
    }
    
    public Tetromino getNextTetromino() {
        return board.getNextPiece();
    }

    public void moveLeft() {
        if (!isGameOver) {
            board.moveLeft();
            System.out.println("Moved tetromino left.");
        }
    }
    
    public void moveAbsDown() {
        if (!isGameOver) {
            board.moveAbsDown();
            System.out.println("Moved tetromino absolutly down.");
        }
    }

    public void moveRight() {
        if (!isGameOver) {
            board.moveRight();
            System.out.println("Moved tetromino right.");
        }
    }

    public void rotateClockwise() {
        if (!isGameOver) {
            board.rotateClockwise();
            System.out.println("Rotated tetromino clockwise.");
        }
    }

    public void rotateCounterClockwise() {
        if (!isGameOver) {
            board.rotateCounterClockwise();
            System.out.println("Rotated tetromino counterclockwise.");
        }
    }

    public TetrisBoard getBoard() {
        return board;
    }

    public ArrayList<Tetromino> getTetrominos() {
        return tetrominos;
    }

    public int getScore() {
        return score;
    }

    public void startGame() {
        isGameOver = false;
        isPaused = false;
        score = 0;
        board.clearBoard();
        tetrominos.clear();
        tetrominos.add(board.getCurrentPiece());
        System.out.println("Game started.");
    }

    public void pauseGame() {
        isPaused = true;
        System.out.println("Game paused.");
    }

    public void resumeGame() {
        isPaused = false;
        System.out.println("Game resumed.");
    }

    public void exitGame() {
        System.out.println("Exiting game.");
        System.exit(0);
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
