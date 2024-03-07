package ceat.game.entity;

import ceat.game.*;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import ceat.game.fx.SkyBeam;

public class Enemy extends BoardEntity {
    public Enemy(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        super.loadSprite("img/baseTile.png");
        sprite.setColor(1, 0, 0, 1);
        sprite.setScale(2);
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
    }

    public void animateEntry() {
        new SkyBeam(game, this)
                .setColor(1f, 0f, 0f)
                .setScale(13f, 150f).play();
        isAnimating = true;
        new Loop(0.2f) {
            public void run(float delta, float elapsed) {
                x = parentTile.x;
                y = parentTile.y + 400 - (elapsed/0.2f)*400;
            }
            public void onEnd() {
                isAnimating = false;
            }
        };
    }

    // epic enemy ai
    public int[] calcStep(boolean allowDiagonals, int stepDistance) {
        int playerX = grid.player.gridX;
        int playerY = grid.player.gridY;

        int newX = gridX;
        int newY = gridY;

        if (playerX > x) newX += stepDistance;
        else if (playerX < x) newX -= stepDistance;

        if (playerY > y) newY += stepDistance;
        else if (playerY < y) newY -= stepDistance;

        int[] xPositions = { gridX, newX, newX };
        int[] yPositions = { newY, gridY, newY };

        ArrayList<Integer> allowedMoves = new ArrayList<>();
        int maxMoveIdx = allowDiagonals ? 2 : 1;

        for (int i = 0; i <= maxMoveIdx; i++) {
            int x = xPositions[i];
            int y = yPositions[i];
            boolean allowMove = true;
            for (Enemy otherEnemy: grid.enemies) {
                if (otherEnemy.gridX == x && otherEnemy.gridY == y) {
                    allowMove = false;
                    break;
                }
            }
            if (allowMove) allowedMoves.add(i);
        }

        if (allowedMoves.isEmpty()) return new int[] {-1, -1};

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
        return new int[] {(int)hi.x, (int)hi.y};
    }

    public void step() {
        int[] newCoords = calcStep(false, 1);
        if (newCoords[0] == -1) return;
        gridX = newCoords[0];
        gridY = newCoords[1];
        super.animateJump(grid.getTileAt(gridX, gridY));
    }
}
