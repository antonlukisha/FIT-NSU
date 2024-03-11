package tetris;

import tetris.controller.GameController;
import tetris.model.GameThread;
import tetris.view.GameFrame;

public class Tetris
{

    public static void main(String[] args)
    {
        GameFrame view = new GameFrame("Tetris");
        GameThread model = new GameThread(view);
        GameController controller = new GameController(view, model);
        controller.startGame();
        view.setVisible(true);
    }
    
}
