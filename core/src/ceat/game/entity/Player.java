package ceat.game.entity;

import ceat.game.ChainedTask;
import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.IntVector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer.Task;

public class Player extends BoardEntity {
    private moveDirection direction = moveDirection.UP;
    private final Highlight highlight;
    private boolean highlightVisible;
    public boolean isAlive;

    public Player(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        setParentTile(getGrid().getTileAt(getGridPosition()));

        super.loadSprite("img/baseTile.png");
        sprite.setColor(1f, 1f, 0f, 1f);
        sprite.setScale(2f);
        sprite.setCenter();

        highlight = new Highlight(newGame, newGrid);

        isAlive = true;

        highlightVisible = true;
        moveHighlight();
    }

    public void setGridPosition(int newGridX, int newGridY) {
        super.setGridPosition(newGridX, newGridY);
        moveHighlight();
    }

    public void setGrid(Grid newGrid) {
        super.setGrid(newGrid);
        super.setGridPosition(Grid.width/2, Grid.height/2);
        highlight.setGrid(newGrid);
        moveHighlight();
    }

    public void kill() {
        isAlive = false;
        // effect stuff here
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
        }
        highlight.setGridPosition(Grid.getFinalPosition(getGridPosition(), offset[0], offset[1]));
    }

    public void setDirection(moveDirection newDirection) {
        direction = newDirection;
        moveHighlight();
    }

    public void step() {
        IntVector2 vec;
        int gridX = getGridPosition().getX();
        int gridY = getGridPosition().getY();
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
                vec = new IntVector2(gridX, gridY);
        }
        getGridPosition().set(vec);
        animateJump(getGrid().getTileAt(gridX, gridY));
        moveHighlight();
    }

    public void animateJump(EmptyTile tile, float duration, float height) {
        super.animateJump(tile, duration, height);
        highlightVisible = false;
        new ChainedTask()
                .wait(0.25f)
                .run(new Task() {
                    @Override
                    public void run() {
                        highlightVisible = true;
                    }
                });
    }

    public void animateJump(EmptyTile tile, float duration) {
        animateJump(tile, duration, 75);
    }

    public void animateJump(EmptyTile tile) {
        animateJump(tile, 0.25f, 75);
    }

    public void draw(SpriteBatch batch) {
        if (!isAlive) return;
        super.draw(batch);
        if (highlightVisible) {
            highlight.render();
            highlight.draw(batch);
        }
    }

    public void dispose() {
        super.dispose();
        highlight.dispose();
    }
}
