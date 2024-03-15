package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.Loop;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EmptyTile extends Entity {
    public float x;
    public float y;

    public EmptyTile(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        super.loadSprite("img/baseTile.png");
        sprite.setColor(0f, 0f, 0f, 0.15f);
        sprite.scale(1.5f);
        sprite.setCenter();
    }

    public void fadeIn() {
        new Loop(1f) {
            public void run(float delta, float elapsed) {
                sprite.setColor(0f, 0f, 0f, 0.15f + (elapsed)*0.25f);
            }

            public void onEnd() {
                sprite.setColor(0f, 0f, 0f, 0.4f);
            }
        };
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(x + ScreenOffset.offsetX, y + ScreenOffset.offsetY);
        super.draw(batch);
    }
}
