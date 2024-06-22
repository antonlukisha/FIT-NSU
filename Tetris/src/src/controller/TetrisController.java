package src.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import src.model.TetrisGame;
import src.view.TetrisView;

public class TetrisController extends Thread {
    private final TetrisView view;
    private final TetrisGame game;

    public TetrisController(TetrisView view, TetrisGame game) {
        this.view = view;
        this.game = game;
        view.addController(new TetrisControlListener());
    }
    
    @Override
    public void run() {
        game.startGame();
        do {
            try {
                game.update();
                view.updateView();
                Thread.sleep(350);
                if (game.isGameOver() == true) {
                    view.showGameOverDialog();
                }
            } catch (InterruptedException exception) {
                System.err.println(exception.getMessage());
            }
        } while (true);
    }

    public class TetrisControlListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent event) {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_LEFT -> game.moveLeft();
                case KeyEvent.VK_RIGHT -> game.moveRight();
                case KeyEvent.VK_UP -> game.rotateClockwise();
                case KeyEvent.VK_DOWN -> game.moveAbsDown();
                case KeyEvent.VK_SPACE -> game.rotateCounterClockwise();
                
                default -> {
                }
            }
            view.repaint();
        }

        @Override
        public void keyTyped(KeyEvent event) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void keyReleased(KeyEvent event) {
            System.out.println("Key released: " + event.getKeyCode());
        }

    }
}
