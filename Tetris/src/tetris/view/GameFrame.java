package tetris.view;

import javax.swing.JFrame;

public final class GameFrame extends JFrame
{
    private final GameArea field;
    public GameFrame(String name)
    {
        super(name);
        initComponents();
        this.field = new GameArea();
        this.add(this.field);
    }

    public void initComponents() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(750, 750);
        this.setResizable(false);
    }
    
    public GameArea getField()
    {
        return this.field;
    }
}
