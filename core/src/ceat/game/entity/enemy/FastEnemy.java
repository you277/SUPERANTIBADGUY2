package ceat.game.entity.enemy;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.IntVector2;

public class FastEnemy extends Enemy {
    public FastEnemy(Game newGame, Grid newGrid) {
        super(newGame, newGrid, EnemyType.FAST);
        getSprite().setColor(0, 0, 1, 1);
    }

    public void animateEntry() {
        animateEntry(0, 0, 1);
    }

    public void step() {
        int[] newCoords = calcStep(false, 2);
        if (newCoords[0] == -1) return;
        IntVector2 newPosition = Grid.getFinalPosition(newCoords[0], newCoords[1]);
        animateJump(getGrid().getTileAt(newPosition));
        setGridPosition(newPosition);
    }

    public String toString() {
        return "BLUE ENEMY";
    }
    public boolean equals(FastEnemy other) {
        return this == other;
    }
}
