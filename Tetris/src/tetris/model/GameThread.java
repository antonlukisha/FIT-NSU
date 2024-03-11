package tetris.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import tetris.view.GameArea;
import tetris.view.GameFrame;
import tetris.view.GameUI;

public class GameThread extends Thread
{
    private final GameArea field;
    private final GameUI ui;
    public GameThread(GameFrame window)
    {
        this.field = window.getField();
        this.ui = this.field.getGUI();
    }
    
    @Override
    public void run()
    {
        while (true)
        {
            field.generateBlock();
            do
            {
                try {
                    field.moveBlockDown();
                    Thread.sleep(400);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (field.isBlockMove() == false)
                {
                    ui.updateScore(destroyCompleteRows());
                    
                }
            }
            while (field.isBlockMove() == true);
        }
    } 
    
    private int destroyCompleteRows()
    {
        boolean isFullRow;
        int destoyedCount = 0;
        for (int i = field.getRows() - 1; i >= 0; --i)
        {
            isFullRow = true;
            for(int x : field.getFieldMatrix()[i])
            { 
                if(x == 0)
                { 
                    isFullRow = false; 
                    break; 
                } 
            } 
            
            
            if (isFullRow == true)
            {
                destoyedCount++;
                field.destroyRows(i);
                i++;
            }
        }
        return destoyedCount;
    }
}
