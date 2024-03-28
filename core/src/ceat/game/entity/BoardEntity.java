package ceat.game.entity;

import ceat.game.*;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.math.Vector2;

public class BoardEntity extends Entity {
    public enum BoardEntityType {
        PLAYER,
        ENEMY,
        PROJECTILE,
        HIGHLIGHT
    }
    public static boolean overlap(BoardEntity a, BoardEntity b) {
        return a.getGridPosition().equals(b.getGridPosition());
    }
    public static boolean overlap(BoardEntity boardEntity, int gridX, int gridY) {
        IntVector2 position = boardEntity.getGridPosition();
        return position.getX() == gridX && position.getY() == gridY;
    }
    private final BoardEntityType type;
    private final Vector2 screenPosition;
    private final IntVector2 gridPosition;
    private EmptyTile parentTile;
    private boolean isAnimating;
    private boolean isJumping;
    public BoardEntity(Game newGame, Grid newGrid, BoardEntityType type) {
        super(newGame, newGrid);
        parentTile = getGrid().getTileAt(0, 0);
        screenPosition = new Vector2();
        gridPosition = new IntVector2();
        this.type = type;
    }
    public BoardEntityType getType() {
        return type;
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
        Vector2 initPosition = previousTile.getScreenPosition();
        isJumping = true;
        new Loop(duration) {
            public void run(float delta, float elapsed) {
                Vector2 nextPosition = nextTile.getScreenPosition();
                float midX = (initPosition.x + nextPosition.x)/2;
                float midY = (initPosition.y + nextPosition.y)/2 + height;
                screenPosition.set(Lerp.threePointBezier(initPosition, new Vector2(midX, midY), nextPosition, elapsed/duration));
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

    public boolean overlaps(BoardEntity other) {
        return gridPosition.equals(other.gridPosition);
    }

    public String toString() {
        return "BOARD ENTITY";
    }
    public boolean equals(BoardEntity other) {
        return parentTile == other.parentTile && gridPosition.equals(other.gridPosition);
    }
}
