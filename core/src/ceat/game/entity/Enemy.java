package ceat.game.entity;

import ceat.game.*;
import ceat.game.fx.EnemyDeathEffect;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import ceat.game.fx.SkyBeam;

public class Enemy extends BoardEntity {
    public Enemy(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        super.loadSprite("img/baseTile.png");
        sprite.setColor(1f, 0f, 0f, 1f);
        sprite.setScale(2f);
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
    }

    public void animateEntry() {
        new SkyBeam(game, this)
                .setColor(1f, 0f, 0f)
                .setScale(13f, 150f).play();
        isAnimating = true;
        new Loop(0.2f) {
            @Override
            public void run(float delta, float elapsed) {
                x = parentTile.x;
                y = parentTile.y + 400 - (elapsed/0.2f)*400;
            }
            @Override
            public void onEnd() {
                isAnimating = false;
            }
        };
    }

    public void animateDeath() {
        new EnemyDeathEffect(game, this).play();
    }

    // epic enemy ai
    @Override
    public void step() {
        int playerX = grid.player.gridX;
        int playerY = grid.player.gridY;

        int newX = gridX;
        int newY = gridY;

        if (playerX > x) newX++;
        else if (playerX < x) newX--;

        if (playerY > y) newY++;
        else if (playerY < y) newY--;

        int[] xPositions = { gridX, newX };
        int[] yPositions = { newY, gridY };

        ArrayList<Integer> allowedMoves = new ArrayList<>();

        for (Enemy otherEnemy: grid.enemies) {
            if (otherEnemy == this) continue;

            int otherX = otherEnemy.gridX;
            int otherY = otherEnemy.gridY;
            for (int i = 0; i < 2; i++) {
                if (xPositions[i] == otherX && yPositions[i] == otherY) continue;
                if (allowedMoves.contains(i)) continue;
                allowedMoves.add(i);
            }
        }

        if (allowedMoves.isEmpty()) return;

        int finalX;
        int finalY;

        if (allowedMoves.size() == 1) {
            finalX = xPositions[0];
            finalY = yPositions[0];
        } else {
            int moveIdx = (int) Math.round(Math.random() * (allowedMoves.size() - 1));
            moveIdx = allowedMoves.get(moveIdx);
            finalX = xPositions[moveIdx];
            finalY = yPositions[moveIdx];
        }

        Vector2 hi = Grid.getFinalPosition(finalX, finalY);
        gridX = (int)hi.x;
        gridY = (int)hi.y;
        super.animateJump(grid.getTileAt(gridX, gridY));
    }
}
