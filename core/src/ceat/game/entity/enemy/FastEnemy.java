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
        gridX = newCoords[0];
        gridY = newCoords[1];
        super.animateJump(grid.getTileAt(gridX, gridY));
    }
}
