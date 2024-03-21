package ceat.game.entity.enemy;

import ceat.game.Game;
import ceat.game.Grid;

public class FastEnemy extends Enemy {
    public FastEnemy(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        getSprite().setColor(0, 0, 1, 1);
    }

    public void animateEntry() {
        animateEntry(0, 0, 1);
    }

    public void step() {
        int[] newCoords = calcStep(false, 2);
        if (newCoords[0] == -1) return;
        animateJump(getGrid().getTileAt(newCoords[0], newCoords[1]));
        setGridPosition(newCoords[0], newCoords[1]);
    }

    public String toString() {
        return "BLUE ENEMY";
    }
    public boolean equals(FastEnemy other) {
        return this == other;
    }
}
