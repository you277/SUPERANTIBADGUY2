package ceat.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

public class EmptyTile extends Entity {
    public final entityType type = entityType.EMPTY;
    public float x;
    public float y;

    public EmptyTile(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        super.loadSprite("img/baseTile.png");
        sprite.setColor(0f, 0f, 0f, 0.15f);
        sprite.scale(1.5f);
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
    }

    public void fadeIn() {
        new Loop(1f) {
            @Override
            public void run(float delta, float elapsed) {
                sprite.setColor(0f, 0f, 0f, 0.15f + (elapsed)*0.25f);
            }
        };
        new ChainedTask().wait(1f).run(new Timer.Task() {
            @Override
            public void run() {
                sprite.setColor(0f, 0f, 0f, 0.4f);
            }
        });
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.setPosition(x, y);
        super.draw(batch);
    }

    @Override
    public void dispose() {
        tex.dispose();
    }
}
