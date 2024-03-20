package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.TexSprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entity {

    public enum moveDirection {
        LEFT,
        UP,
        RIGHT,
        DOWN
    }

    public TexSprite sprite;
    private boolean loadedSprite;
    private Grid grid;
    private Game game;

    public Entity(Game newGame, Grid newGrid) {
        grid = newGrid;
        game = newGame;
    }

    public void loadSprite(String path) {
        sprite = new TexSprite(path);
        loadedSprite = true;
    }
    public void setGrid(Grid newGrid) {
        grid = newGrid;
    }
    public Grid getGrid() {
        return grid;
    }
    public Game getGame() {
        return game;
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
