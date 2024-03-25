package ceat.game.titleScreenGui;

import ceat.game.Game;

public class BackgroundGameAI {
    private Game game;
    public BackgroundGameAI(Game game) {
        this.game = game;
        game.getPlayer().kill();
        for (int i = 0; i < 5; i++) {
            game.spawnEnemies();
        }
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
