package ceat.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer.Task;

import ceat.game.ChainedTask;

public class Player extends BoardEntity {
    public final entityType type = entityType.PLAYER;
    private moveDirection direction = moveDirection.UP;
    private final BoardEntity highlight;
    private boolean highlightVisible;


    public Player(TheActualGame newGame, Grid newGrid) {
        super(newGame, newGrid);
        parentTile = grid.getTileAt(gridX, gridY);

        super.loadSprite("img/baseTile.png");
        sprite.setColor(1f, 1f, 0f, 1f);
        sprite.setScale(2f);
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);

        highlight = new Highlight(newGame, newGrid);

        highlightVisible = true;
        moveHighlight();
    }
    @Override
    public void setGridPosition(int newGridX, int newGridY) {
        super.setGridPosition(newGridX, newGridY);
        moveHighlight();
    }

    public moveDirection getDirection() {
        return direction;
    }
    int[] left = {-1, 0};
    int[] up = {0, -1};
    int[] right = {1, 0};
    int[] down = {0, 1};

    private void moveHighlight() {
        int[] offset = left;
        switch (direction) {
            case UP:
                offset = up;
                break;
            case RIGHT:
                offset = right;
                break;
            case DOWN:
                offset = down;
                break;
            case LEFT:
                offset = left; // INTELLISENSE WHAT DO YOU WANT???
                break;
        }
        Vector2 nextPos = Grid.getFinalPosition(gridX + offset[0], gridY + offset[1]);
        highlight.setGridPosition((int)nextPos.x, (int)nextPos.y);
    }

    public void setDirection(moveDirection newDirection) {
        direction = newDirection;
        moveHighlight();
    }

    @Override
    public void step() {
        Vector2 vec;
        switch(direction) {
            case LEFT:
                vec = Grid.getFinalPosition(gridX - 1, gridY);
                break;
            case UP:
                vec = Grid.getFinalPosition(gridX, gridY - 1);
                break;
            case RIGHT:
                vec = Grid.getFinalPosition(gridX + 1, gridY);
                break;
            case DOWN:
                vec = Grid.getFinalPosition(gridX, gridY + 1);
                break;
            default:
                vec = new Vector2();
                vec.set(gridX, gridY);
        }
        gridX = (int)vec.x;
        gridY = (int)vec.y;
        super.animateJump(grid.getTileAt(gridX, gridY));
        highlightVisible = false;
        new ChainedTask()
            .wait(0.25f)
            .run(new Task() {
                @Override
                public void run() {
                    highlightVisible = true;
                }
            });
        moveHighlight();
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.render();
        super.draw(batch);
        if (highlightVisible) {
            highlight.render();
            highlight.draw(batch);
        }
    }

    @Override
    public void dispose() {
        tex.dispose();
        highlight.dispose();
    }
}
