package ceat.game.titleScreenGui;

import ceat.game.Game;

public class BackgroundGameAI {
    private Game game;
    public BackgroundGameAI(Game game) {
        this.game = game;
    }

    public void step() {

    }

    public String toString() {
        return "BACKGROUND GAME AI";
    }
    public boolean equals(BackgroundGameAI other) {
        return this == other;
    }
}
