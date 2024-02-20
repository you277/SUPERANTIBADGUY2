package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Entity {

    public enum moveDirection {
        LEFT,
        UP,
        RIGHT,
        DOWN
    }

    public Texture tex;
    public Sprite sprite;
    private boolean loadedSprite;
    public Grid grid;
    public Game game;

    public Entity(Game newGame, Grid newGrid) {
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

    public void loadSprite(String path) {
        tex = new Texture(path);
        sprite = new Sprite(tex);
        loadedSprite = true;
    }

    public void render() {}

    public void step() {}

    public void draw(SpriteBatch batch) {
        if (loadedSprite) sprite.draw(batch);
    }

    public void dispose() {
        tex.dispose();
    }
}
