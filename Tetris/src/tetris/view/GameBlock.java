package tetris.view;

import java.awt.Color;

public class GameBlock
{
    private int[][] shape;
    private final Color color;
    private int angle;
    private int x;
    private int y;
    public GameBlock(char shapeID, Color color, int angle)
    {
        this.x = 4;
        this.y = -2;
        this.angle = angle;
        switch(shapeID)
        {
            case 'T':
                this.shape = new int[][]{{0, 0, 0},{1, 1, 1},{0, 1, 0}};
                rotateShape(this.shape, angle);
                break;
            case 'O':
                this.shape = new int[][]{{1, 1},{1, 1}};
                rotateShape(this.shape, angle);
                break;
            case 'L':
                this.shape = new int[][]{{0, 1, 0},{0, 1, 0},{0, 1, 1}};
                rotateShape(this.shape, angle);
                break;
            case 'J':
                this.shape = new int[][]{{0, 1, 0},{0, 1, 0},{1, 1, 0}};
                rotateShape(this.shape, angle);
                break;
            case 'I':
                this.shape = new int[][]{{0, 1, 0, 0},{0, 1, 0, 0},{0, 1, 0, 0},{0, 1, 0, 0}};
                rotateShape(this.shape, angle);
                break;
            case 'S':
                this.shape = new int[][]{{0, 1, 1},{1, 1, 0},{0, 0, 0}};
                rotateShape(this.shape, angle);
                break;
            case 'Z':
                this.shape = new int[][]{{1, 1, 0},{0, 1, 1},{0, 0, 0}};
                rotateShape(this.shape, angle);
                break;
            default:
                break;
        }
        this.color = color;
    }
    
    public GameBlock(GameBlock block)
    {
        this.x = 4;
        this.y = -2;
        this.shape = block.getShape();
        rotateShape(this.shape, block.getAngle());
        this.color = block.getColor();
    }
    
    public int[][] getShape()
    {
        return this.shape;
    }
    
    public int getAngle()
    {
        return this.angle;
    }
    
    public Color getColor()
    {
        return this.color;
    }

    public int getHeight()
    {
        return this.shape.length;
    }
    
    public int getWidth()
    {
        return this.shape[0].length;
    }
    
    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }
    
    public void rotateShape(int[][] shape, int angle)
    {
        if (angle == 0)
        {
            return;
        }
        int[][] rotatedShape = new int[this.getHeight()][this.getWidth()];
        for (int i = 0; i < this.getWidth(); i++)
        {
            for (int j = 0; j < this.getHeight(); j++)
            {
                switch (angle) {
                case 1:
                    rotatedShape[j][i] = shape[this.getWidth() - i - 1][j];
                    break;
                case 2:
                    rotatedShape[i][j] = shape[this.getWidth() - i - 1][this.getHeight() - j - 1];
                    break;
                default:
                    rotatedShape[j][i] = shape[i][this.getHeight() - j - 1];
                    break;
                }
            }
        }
        copyMatrix(this.getHeight(), this.getWidth(), shape, rotatedShape);
    }

    private void copyMatrix(int height, int width, int[][] in, int[][] out)
    {
        for (int i = 0; i < width; i++)
        {
            System.arraycopy(out[i], 0, in[i], 0, height);
        }
    }
    
    public void moveDown()
    {
        this.y++;
    }
    
    public void moveRight()
    {
        this.x++;
    }
    
    public void moveLeft()
    {
        this.x--;
    }
}
