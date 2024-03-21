package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.Loop;
import ceat.game.TexSprite;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class EmptyTile extends Entity {
    private final Vector2 screenPosition;

    public EmptyTile(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        TexSprite sprite = loadSprite("img/baseTile.png");
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
                getSprite().setColor(0f, 0f, 0f, 0.15f + (elapsed)*0.25f);
            }

            public void onEnd() {
                getSprite().setColor(0f, 0f, 0f, 0.4f);
            }
        };
    }

    public void draw(SpriteBatch batch) {
        getSprite().setPosition(ScreenOffset.project(getScreenPosition()));
        super.draw(batch);
    }

    public String toString() {
        return "EMPTY TILE";
    }
    public boolean equals(EmptyTile other) {
        return this == other;
    }
}
