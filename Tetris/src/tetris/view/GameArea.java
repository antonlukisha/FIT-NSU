package tetris.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JPanel;

public final class GameArea extends JPanel
{
    private boolean RIGHT_BOUND;
    private boolean LEFT_BOUND;
    private boolean BOTTOM_BOUND;
    private boolean NO_BOUND;
    
    private final Color DARK_GREY = new Color(24, 27, 31);
    private final Color LIGHT_GREY = new Color(36, 41, 47);
    private final Color GREY = new Color(47, 53, 61);
    private final Color RED = new Color(185, 10, 25);
    private final Color YELLOW = new Color(231, 175, 0);
    private final Color ORANGE = new Color(243, 105, 0);
    private final Color CYAN = new Color(0, 208, 208);
    private final Color PURPLE = new Color(106, 27, 255);
    private final Color GREEN = new Color(43, 196, 11);
    private final Color BLUE = new Color(0, 133, 255);
    
    private final int Columns;
    private final int Rows;
    private final int CellSize;
    private GameBlock block;
    private final GameUI ui;
    private final int[][] fieldMatrix; 
    private final Random randColor = new Random();
    private int randFirst;
    private int randSecond;
    
    public GameArea()
    {
        this.setBounds(250, 5, 250, 550);
        this.setBackground(GREY);
        this.ui = new GameUI();
        Columns = 10;
        CellSize = this.getBounds().width / Columns;
        Rows = this.getBounds().height / CellSize;
        fieldMatrix = new int[Rows + 1][Columns + 2];
        RIGHT_BOUND = false;
        LEFT_BOUND = false;
        BOTTOM_BOUND = false;
        NO_BOUND = true;
        randFirst = randColor.nextInt(7) + 1;
        fillField();
        
    }
    
    private void fillField()
    {
        for (int i = 0; i < Rows; i++)
        {
            fieldMatrix[i][0] = 1;
            fieldMatrix[i][Columns + 1] = 1;
        }
        for (int i = 0; i <= Columns + 1; i++)
        {
            fieldMatrix[Rows][i] = 1;
        }
    }
    
    public boolean isBlockMove()
    {
        return (BOTTOM_BOUND != true);
    }
    
    public void moveBlockDown()
    {
        this.reachedTouch();
        if (BOTTOM_BOUND == false)
        {
            block.moveDown();
        }
        repaint();
    }
    
    public void moveBlockRight()
    {
        this.reachedTouch();
        if (NO_BOUND == true && RIGHT_BOUND == false)
        {
            block.moveRight();
        }
        repaint();
    }
    
    public void moveBlockDownFast()
    {
        while (true)
        {
            this.reachedTouch();
            if (BOTTOM_BOUND == false)
            {
                block.moveDown();
            }
            else
            {
                break;
            }
        }
        repaint();
    }
    
    public void moveBlockLeft()
    {
        this.reachedTouch();
        if (NO_BOUND == true && LEFT_BOUND == false)
        {
            block.moveLeft();
        }
        repaint();
    }
    
    public void moveBlockRotate()
    {
        if (BOTTOM_BOUND == false && adjustBlockPos())
        {
            adjustBlockPos();
            block.rotateShape(block.getShape(), 1);
            
            
        }
        repaint();
    }
    
    
    private boolean adjustBlockPos()
    {
        GameBlock tempBlock = new GameBlock(block);
        tempBlock.rotateShape(tempBlock.getShape(), 1);
        int colisionType = isColision();
        
        if (colisionType == 0)
        {
            return true;
        }
        int iterCount = 0;
        if (colisionType == 1)
        {
            while (isColision() != 0)
            {
                tempBlock.moveRight();
                if (iterCount > 1)
                {
                    return false;
                }
                iterCount++;
            }
            for (int i = 0; i <= iterCount; i++)
            {
                block.moveRight();
            }
            
        }
        else if (colisionType == 2)
        {
            while (isColision() != 0)
            {
                tempBlock.moveLeft();
                if (iterCount > 1)
                {
                    return false;
                }
                iterCount++;
            }
            for (int i = 0; i <= iterCount; i++)
            {
                block.moveLeft();
            }
        }
        return true;
    }
    
    private int isColision()
    {
        for (int i = 0; i < block.getWidth(); ++i)
        {            
            for (int j = 0; j < block.getHeight(); ++j)
            {
                if (fieldMatrix[block.getY() + j][block.getX() + i + 1] != 0)
                {
                    return (i > (block.getWidth() / 2)) ? 2 : 1;
                }
            }
        }
        return 0;
    }
    
    private void reachedTouch()
    {
        RIGHT_BOUND = reachedTouchRight();
        LEFT_BOUND = reachedTouchLeft();
        
        if (reachedTouchBottom() == true)
        {
            BOTTOM_BOUND = true;
            NO_BOUND = false;
        }
    }
    
    private boolean reachedTouchBottom()
    {
        for (int i = 0; i < block.getWidth(); ++i)
        {            
            int j;
            for (j = block.getHeight() - 1; j >= 0; --j)
            {
                if (block.getShape()[j][i] == 1 || j == 0)
                {
                    break;
                }
            }
            
            if (block.getShape()[j][i] == 0)
            {
                continue;
            }
                
            try
            {
                if (fieldMatrix[block.getY() + j + 1][block.getX() + i + 1] != 0)
                {
                    return true;
                }
            } 
            catch(ArrayIndexOutOfBoundsException e)
            {}
            
        }
        return false;
    }
    
    private boolean reachedTouchRight()
    {
        for (int i = 0; i < block.getHeight(); ++i)
        {            
            int j;
            for (j = block.getWidth() - 1; j >= 0; --j)
            {
                if (block.getShape()[j][i] == 1 || j == 0)
                {
                    break;
                }
            }
            
            if (block.getShape()[j][i] == 0)
            {
                continue;
            }
            try
            {
                if (fieldMatrix[block.getY() + j][block.getX() + i + 2] != 0)
                {
                    return true;
                }
            } 
            catch(ArrayIndexOutOfBoundsException e)
            {}
            
        }
        return false;
    }
    
    private boolean reachedTouchLeft()
    {
        for (int i = 0; i < block.getHeight(); ++i)
        {            
            int j;
            for (j = 0; j < block.getWidth() - 1; ++j)
            {
                if (block.getShape()[j][i] == 1 || j == (block.getWidth() - 1))
                {
                    break;
                }
            }
            
            if (block.getShape()[j][i] == 0)
            {
                continue;
            }
            
            try
            {
                if (fieldMatrix[block.getY() + j][block.getX() + i] != 0)
                {
                    return true;
                }
            } 
            catch(ArrayIndexOutOfBoundsException e)
            {}
            
        }
        return false;
    }
    
    public void generateBlock()
    {
        BOTTOM_BOUND = false;
        NO_BOUND = true;
        randSecond = randColor.nextInt(7) + 1;
        block = new GameBlock(getShapeFromNum(randFirst), getColorFromNum(randFirst), randFirst % 4);
        randFirst = randSecond;
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (int i = 1; i <= Columns; i++)
        {
            for (int j = 0; j < Rows; j++)
            {
                if (this.fieldMatrix[j][i] == 0)
                {
                    g.setColor(LIGHT_GREY);
                }
                else
                {
                    g.setColor(getColorFromNum(this.fieldMatrix[j][i]));
                }
                g.fillRect(250 + (i - 1) * CellSize, 5 + j * CellSize, CellSize, CellSize);
                g.setColor(DARK_GREY);
                g.drawRect(250 + (i - 1) * CellSize, 5 + j * CellSize, CellSize, CellSize);
            }
        }
        paintBlock(g);
        paintNextBlock(g);
        paintScore(g);
    }

    private void paintBlock(Graphics g)
    {
        int height = block.getHeight();
        int width = block.getWidth();
        Color color = block.getColor();
        int[][] shape = block.getShape();
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                if(shape[i][j] == 1 && block.getY() >= 0)
                {
                    int x = (j + block.getX()) * CellSize;
                    int y = (i + block.getY()) * CellSize;
                    g.setColor(color);
                    g.fillRect(250 + x, 5 + y, CellSize, CellSize);
                    g.setColor(DARK_GREY);
                    g.drawRect(250 + x, 5 + y, CellSize, CellSize);
                    if (BOTTOM_BOUND == true)
                    {
                        fieldMatrix[i + block.getY()][j + block.getX() + 1] = getNumFromColor(color);
                    }
                }
            }
        }
    }

    private void paintNextBlock(Graphics g)
    {
        GameBlock blockShow = new GameBlock(getShapeFromNum(randSecond), getColorFromNum(randSecond), 0);
        Color color = blockShow.getColor();
        int[][] shape = new int[4][4];
        putShapeToBox(shape, blockShow.getShape());
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if(shape[i][j] == 1)
                {
                    g.setColor(color);
                }
                else
                {
                    g.setColor(LIGHT_GREY);
                }
                g.fillRect(100 + j * CellSize, 100 + i * CellSize, CellSize, CellSize);
                g.setColor(DARK_GREY);
                g.drawRect(100 + j * CellSize, 100 + i * CellSize, CellSize, CellSize);
            }
        }
    }
    
    private void paintScore(Graphics g) {
        g.setFont(new Font("Montserrat", 0, 25));
        g.setColor(Color.WHITE);
        g.drawString(ui.getScore(), 555, 100);
    }
    
    private void putShapeToBox(int[][] box, int[][] shape)
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                switch(shape.length)
                {
                    case 4:
                        box[i][j] = shape[i][j];
                        break;
                    case 3:
                        if (i > 0 && j > 0)
                        {
                            box[i][j] = shape[i - 1][j - 1];
                        }
                        break;
                    case 2:
                        if ((i > 0 && j > 0) && (i < 3 && j < 3))
                        {
                            box[i][j] = shape[i - 1][j - 1];
                        }
                        break;
                    default:
                        break;
                }
                    
            }
        }
    }
    
    private Color getColorFromNum(int num)
    {
        return switch (num) {
            case 1 -> RED;
            case 2 -> YELLOW;
            case 3 -> ORANGE;
            case 4 -> CYAN;
            case 5 -> PURPLE;
            case 6 -> GREEN;
            default -> BLUE;
        };
    }
    
    private char getShapeFromNum(int num)
    {
        return switch (num) {
            case 1 -> 'Z';
            case 2 -> 'O';
            case 3 -> 'L';
            case 4 -> 'I';
            case 5 -> 'T';
            case 6 -> 'S';
            default -> 'J';
        };
    }
    
    private int getNumFromColor(Color color)
    {
        if (color == RED)
        {
            return 1;
        }
        else if (color == YELLOW)
        {
            return 2;
        }
        else if (color == ORANGE)
        {
            return 3;
        }
        else if (color == CYAN)
        {
            return 4;
        }
        else if (color == PURPLE)
        {
            return 5;
        }
        else if (color == GREEN)
        {
            return 6;
        }
        return 7;
    }
    
    public void destroyRows(int idx)
    {
        destroyRow(idx);
        shiftRow(idx); 
        repaint();
    }
    
    private void destroyRow(int idx)
    {
        for (int i = 1; i <= Columns; ++i)
        {
            this.fieldMatrix[idx][i] = 0;
        }
    }
    
    private void shiftRow(int idx)
    {
        for (int i = idx; i > 0; --i)
        {
            for (int j = 1; j <= Columns; ++j)
            {
                this.fieldMatrix[i][j] = this.fieldMatrix[i - 1][j];
            }
        }
    }
    
    public int getRows()
    {
        return this.Rows;
    }
    
    public int getColumns()
    {
        return this.Columns;
    }
    
    public int[][] getFieldMatrix()
    {
        return this.fieldMatrix;
    }
    
    public GameUI getGUI()
    {
        return this.ui;
    }
}
