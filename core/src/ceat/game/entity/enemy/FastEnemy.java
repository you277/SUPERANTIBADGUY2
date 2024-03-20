package ceat.game.entity.enemy;

import ceat.game.Game;
import ceat.game.Grid;

public class FastEnemy extends Enemy {
    public FastEnemy(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        sprite.setColor(0, 0, 1, 1);
    }

    public void animateEntry() {
        animateEntry(0, 0, 1);
    }

    public void step() {
        int[] newCoords = calcStep(false, 2);
        if (newCoords[0] == -1) return;
        setGridPosition(newCoords[0], newCoords[1]);
        super.animateJump(getGrid().getTileAt(getGridPosition()));
    }

    public String toString() {
        return "BLUE ENEMY";
    }
}
