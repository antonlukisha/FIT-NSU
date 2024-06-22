package src.controller;

import src.model.HighScores;
import src.view.HighScoresView;

public class HighScoresController {
    private HighScoresView highScoresView;
    private HighScores highScores;

    public HighScoresController(HighScoresView highScoresView, HighScores highScores) {
        this.highScoresView = highScoresView;
        this.highScores = highScores;
    }
}
