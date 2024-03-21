package ceat.game.entity;

import ceat.game.*;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.math.Vector2;

public class BoardEntity extends Entity {
    public static boolean overlap(BoardEntity a, BoardEntity b) {
        return a.getGridPosition().equals(b.getGridPosition());
    }
    public static boolean overlap(BoardEntity boardEntity, int gridX, int gridY) {
        IntVector2 position = boardEntity.getGridPosition();
        return position.getX() == gridX && position.getY() == gridY;
    }
    private final Vector2 screenPosition;
    private final IntVector2 gridPosition;
    private EmptyTile parentTile;
    private boolean isAnimating;
    private boolean isJumping;
    public BoardEntity(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        parentTile = getGrid().getTileAt(0, 0);
        screenPosition = new Vector2();
        gridPosition = new IntVector2();
    }
    public void setGridPosition(int newGridX, int newGridY) {
        gridPosition.set(newGridX, newGridY);
        parentTile = getGrid().getTileAt(newGridX, newGridY);
    }
    public void setGridPosition(IntVector2 position) {
        setGridPosition(position.getX(), position.getY());
    }
    public void setParentTile(EmptyTile tile) {
        parentTile = tile;
    }
    public EmptyTile getParentTile() {
        return parentTile;
    }
    public IntVector2 getGridPosition() {
        return gridPosition;
    }
    public Vector2 getScreenPosition() {
        return screenPosition;
    }
    public boolean getIsJumping() {
        return isJumping;
    }
    public void setIsAnimating(boolean isAnimating) {
        this.isAnimating = isAnimating;
    }
    public void render() {
        if (!isAnimating) {
            Vector2 spritePos = getGrid().getSpritePositionFromGridPosition(gridPosition.getX(), gridPosition.getY());
            screenPosition.set(spritePos.x, spritePos.y);
        }
        getSprite().setPosition(ScreenOffset.project(screenPosition));
    }

    public void animateJump(EmptyTile previousTile, EmptyTile nextTile, float duration, float height) {
        isAnimating = true;
        float initX = previousTile.getScreenPosition().x;
        float initY = previousTile.getScreenPosition().y;
        isJumping = true;
        new Loop(duration) {
            public void run(float delta, float elapsed) {
                float midX = (initX + nextTile.getScreenPosition().x)/2;
                float midY = (initY + nextTile.getScreenPosition().y)/2 + height;
                screenPosition.set(Lerp.threePointBezier(initX, initY, midX, midY, nextTile.getScreenPosition().x, nextTile.getScreenPosition().y, elapsed/duration));
            }
            public void onEnd() {
                isAnimating = false;
                isJumping = false;
            }
        };
    }
    public void animateJump(EmptyTile nextTile) {
        animateJump(parentTile, nextTile, 0.25f, 75);
    }

    public String toString() {
        return "BOARD ENTITY";
    }
    public boolean equals(BoardEntity other) {
        return parentTile == other.parentTile && gridPosition.equals(other.gridPosition);
    }
}
