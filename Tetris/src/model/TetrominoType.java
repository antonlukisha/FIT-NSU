
import java.awt.Color;

public enum TetrominoType {
    I_TETROMINO(new int[][] {
            { 0, 0, 0, 0 },
            { 1, 1, 1, 1 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }
        }, new Color(0, 255, 255)),
    J_TETROMINO(new int[][] {
            { 1, 0, 0 },
            { 1, 1, 1 },
            { 0, 0, 0 }
        }, new Color(0, 0, 255)),
    L_TETROMINO(new int[][] {
            { 0, 0, 1 },
            { 1, 1, 1 },
            { 0, 0, 0 }
        }, new Color(255, 127, 0)),
    O_TETROMINO(new int[][] {
            { 1, 1 },
            { 1, 1 }
        }, new Color(255, 255, 0)),
    S_TETROMINO(new int[][] {
            { 0, 1, 1 },
            { 1, 1, 0 },
            { 0, 0, 0 }
        }, new Color(0, 255, 0)),
    T_TETROMINO(new int[][] {
            { 0, 1, 0 },
            { 1, 1, 1 },
            { 0, 0, 0 }
        }, new Color(128, 0, 128)),
    Z_TETROMINO(new int[][] {
            { 1, 1, 0 },
            { 0, 1, 1 },
            { 0, 0, 0 }
        }, new Color(255, 0, 0));

    private int[][] shape;
    private Color color;

    private TetrominoType(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public int[][] getShape() {
        return shape;
    }
    
    public Color getColor() {
        return color;
    }
}
