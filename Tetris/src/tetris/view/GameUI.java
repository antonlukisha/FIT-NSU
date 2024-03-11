package tetris.view;

public class GameUI
{
    private String score = new String();
    public GameUI()
    {
        score = "Score: 0";
    }
    
    public void updateScore(int destoyedCount)
    {
        int scoreCount = switch (destoyedCount) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };
        
        score = "Score: " + String.valueOf(Integer.parseInt(score.substring(7)) + scoreCount);
    }

    public String getScore() {
        return this.score;
    }
}
