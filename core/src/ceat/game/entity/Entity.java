package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.TexSprite;
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

    public TexSprite sprite;
    private boolean loadedSprite;
    public Grid grid;
    public Game game;

    public Entity(Game newGame, Grid newGrid) {
        grid = newGrid;
        game = newGame;
    }

    public void loadSprite(String path) {
        sprite = new TexSprite(path);
        loadedSprite = true;
    }

    public void render() {}

    public void step() {}

    public void draw(SpriteBatch batch) {
        if (loadedSprite) sprite.draw(batch);
    }

    public void dispose() {
        sprite.dispose();
    }
}
