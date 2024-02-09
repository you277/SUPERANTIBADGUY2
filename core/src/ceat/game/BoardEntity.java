package ceat.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import ceat.game.Loop;
import ceat.game.ChainedTask;
import ceat.game.Lerp;

public class BoardEntity extends Entity {
    public float x;
    public float y;
    public int gridX;
    public int gridY;
    public EmptyTile parentTile;
    private boolean isAnimating;
    public BoardEntity(TheActualGame newGame, Grid newGrid) {
        super(newGame, newGrid);
    }
    public void render() {
        if (!isAnimating) {
            Vector2 spritePos = grid.getSpritePositionFromGridPosition(gridX, gridY);
            sprite.setPosition(spritePos.x, spritePos.y);
        }
    }

    public void animateJump(EmptyTile nextTile) {
        isAnimating = true;
        new Loop(0.5f) {
            @Override
            public void run(float delta, float elapsed) {
                float midX = (parentTile.x + nextTile.x)/2;
                float midY = (parentTile.y + nextTile.y)/2 + 100;
                Vector2 newPos = Lerp.threePointBezier(parentTile.x, parentTile.y, midX, midY, nextTile.x, nextTile.y, elapsed*2);
                x = newPos.x;
                y = newPos.y;
            }
        };
        new ChainedTask()
            .wait(0.5f)
            .run(new Task() {
            @Override
            public void run() {
                isAnimating = false;
                parentTile = nextTile;
            }
        });
    }
}
