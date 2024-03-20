package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.Loop;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class EmptyTile extends Entity {
    private final Vector2 screenPosition;

    public EmptyTile(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        super.loadSprite("img/baseTile.png");
        sprite.setColor(0f, 0f, 0f, 0.15f);
        sprite.scale(1.5f);
        sprite.setCenter();
        screenPosition = new Vector2();
    }
    public Vector2 getScreenPosition() {
        return screenPosition;
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
        Vector2 position = getScreenPosition();
        sprite.setPosition(position.x + ScreenOffset.offsetX, position.y + ScreenOffset.offsetY);
        super.draw(batch);
    }
}
