package ceat.game.entity.enemy;

import ceat.game.*;
import ceat.game.entity.BoardEntity;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import ceat.game.fx.SkyBeam;

public class Enemy extends BoardEntity {
    public Enemy(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        super.loadSprite("img/baseTile.png");
        sprite.setColor(1, 0, 0, 1);
        sprite.setScale(2);
        sprite.setCenter();
    }

    public void animateEntry(float r, float g, float b) {
        new SkyBeam( this)
                .setColor(r, g, b)
                .setScale(13f, 150f).play();
        setIsAnimating(true);
        new Loop(0.2f) {
            public void run(float delta, float elapsed) {
                Vector2 screenPosition = getParentTile().getScreenPosition();
                getScreenPosition().set(screenPosition.x, screenPosition.y + 400 - (elapsed/0.2f)*400);
            }
            public void onEnd() {
                setIsAnimating(false);
            }
        };
    }

    public void animateEntry() {
        animateEntry(1, 0, 0);
    }

    // epic enemy ai
    public int[] calcStep(boolean allowDiagonals, int stepDistance) {
        IntVector2 playerPosition = getGrid().getPlayer().getGridPosition();
        int playerX = playerPosition.getX();
        int playerY = playerPosition.getY();

        IntVector2 gridPosition = getGridPosition();

        int newX = getGridPosition().getX();
        int newY = getGridPosition().getY();

        if (playerX > newX) newX += stepDistance;
        else if (playerX < newX) newX -= stepDistance;

        if (playerY > newY) newY += stepDistance;
        else if (playerY < newY) newY -= stepDistance;

        int[] xPositions = { gridPosition.getX(), newX, newX };
        int[] yPositions = { newY, gridPosition.getY(), newY };

        ArrayList<Integer> allowedMoves = new ArrayList<>();
        int maxMoveIdx = allowDiagonals ? 2 : 1;

        for (int i = 0; i <= maxMoveIdx; i++) {
            boolean allowMove = true;
            for (Enemy otherEnemy: getGrid().getEnemies()) {
                if (otherEnemy.getGridPosition().equals(gridPosition)) {
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

        IntVector2 hi = Grid.getFinalPosition(finalX, finalY);
        return new int[] {hi.getX(), hi.getY()};
    }

    public void step() {
        int[] newCoords = calcStep(false, 1);
        if (newCoords[0] == -1) return;
        getGridPosition().set(newCoords[0], newCoords[1]);
        super.animateJump(getGrid().getTileAt(getGridPosition()));
    }

    public String toString() {
        return "RED ENEMY";
    }
}
