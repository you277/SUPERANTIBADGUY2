package ceat.game.entity;

import ceat.game.*;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.math.Vector2;

public class BoardEntity extends Entity {
    public static boolean overlap(BoardEntity a, BoardEntity b) {
        return a.gridX == b.gridX && a.gridY == b.gridY;
    }
    public static boolean overlap(BoardEntity boardEntity, int gridX, int gridY) {
        return boardEntity.gridX == gridX && boardEntity.gridY == gridY;
    }
    public float x;
    public float y;
    public int gridX;
    public int gridY;
    public EmptyTile parentTile;
    public boolean isAnimating;
    public BoardEntity(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        parentTile = grid.getTileAt(0, 0);
    }
    public void setGridPosition(int newGridX, int newGridY) {
        gridX = newGridX;
        gridY = newGridY;
        parentTile = grid.getTileAt(newGridX, newGridY);
    }
    public void render() {
        if (!isAnimating) {
            Vector2 spritePos = grid.getSpritePositionFromGridPosition(gridX, gridY);
            x = spritePos.x;
            y = spritePos.y;
        }
        sprite.setPosition(x + ScreenOffset.offsetX, y + ScreenOffset.offsetY);
    }

    public void animateJump(EmptyTile nextTile, float duration, float height) {
        isAnimating = true;
        float initX = parentTile.x;
        float initY = parentTile.y;
        new Loop(duration) {
            public void run(float delta, float elapsed) {
                float midX = (initX + nextTile.x)/2;
                float midY = (initY + nextTile.y)/2 + height;
                Vector2 newPos = Lerp.threePointBezier(initX, initY, midX, midY, nextTile.x, nextTile.y, elapsed/duration);
                x = newPos.x;
                y = newPos.y;
            }
            public void onEnd() {
                isAnimating = false;
                parentTile = nextTile;
            }
        };
    }

    public void animateJump(EmptyTile nextTile, float duration) {
        animateJump(nextTile, duration, 75);
    }

    public void animateJump(EmptyTile nextTile) {
        animateJump(nextTile, 0.25f, 75);
    }
}
