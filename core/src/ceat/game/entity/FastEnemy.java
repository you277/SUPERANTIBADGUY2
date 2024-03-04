package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.Loop;
import ceat.game.fx.SkyBeam;

public class FastEnemy extends Enemy {
    public FastEnemy(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        sprite.setColor(0, 0, 1, 1);
    }

    public void animateEntry() {
        new SkyBeam(game, this)
                .setColor(0, 0, 1)
                .setScale(13, 150).play();
        isAnimating = true;
        new Loop(0.2f) {
            public void run(float delta, float elapsed) {
                x = parentTile.x;
                y = parentTile.y + 400 - (elapsed / 0.2f) * 400;
            }

            public void onEnd() {
                isAnimating = false;
            }
        };
    }

    public void step() {
        int[] newCoords = calcStep(true);
        if (newCoords[0] == -1) return;
        gridX = newCoords[0];
        gridY = newCoords[1];
        super.animateJump(grid.getTileAt(gridX, gridY));
    }
}
