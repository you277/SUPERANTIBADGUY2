package ceat.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Entity {
    public enum entityType {
        EMPTY,
        PLAYER,
        ENEMY,
        PROJECTILE,
        HIGHLIGHT
    }

    public enum moveDirection {
        LEFT,
        UP,
        RIGHT,
        DOWN
    }

    public final entityType type = entityType.EMPTY;

    public Texture tex;
    public Sprite sprite;
    private boolean loadedSprite;
    public Grid grid;
    public TheActualGame game;

    public Entity(TheActualGame newGame, Grid newGrid) {
        grid = newGrid;
        game = newGame;
    }

    public Sprite loadSprite(String path, float scaleX, float scaleY) {
        tex = new Texture(path);
        sprite = new Sprite(tex);
        sprite.setScale(scaleX, scaleY);
        loadedSprite = true;
        return sprite;
    }

    public Sprite loadSprite(String path) {
        tex = new Texture(path);
        sprite = new Sprite(tex);
        loadedSprite = true;
        return sprite;
    }

    public void render() {}

    public void step() {}

    public void draw(SpriteBatch batch) {
        if (loadedSprite) sprite.draw(batch);
    }

    public void dispose() {}
}
