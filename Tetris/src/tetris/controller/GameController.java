package tetris.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import tetris.model.GameThread;
import tetris.view.GameFrame;

public final class GameController
{
    private final GameFrame view;
    private final GameThread model;
    
    public GameController(GameFrame view, GameThread model)
    {
        this.view = view;
        this.model = model;
        initController();
    }
    
    public void startGame()
    {
        model.start();
    }
    
    private void initController()
    {
        view.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                int keyCode = event.getKeyCode();
                if (keyCode == KeyEvent.VK_UP) {
                    view.getField().moveBlockRotate();
                }
                else if (keyCode == KeyEvent.VK_DOWN) {
                    view.getField().moveBlockDownFast();
                }
                else if (keyCode == KeyEvent.VK_LEFT) {
                    view.getField().moveBlockLeft();
                }
                else if (keyCode == KeyEvent.VK_RIGHT) {
                    view.getField().moveBlockRight();
                }
            }
        });
    }
}
