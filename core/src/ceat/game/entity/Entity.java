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

    private TexSprite sprite;
    private Grid grid;
    private final Game game;

    public Entity(Game newGame, Grid newGrid) {
        grid = newGrid;
        game = newGame;
    }

    public TexSprite loadSprite(String path) {
        sprite = new TexSprite(path);
        return sprite;
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
    public TexSprite getSprite() {
        return sprite;
    }

    public void render() {}

    public void step() {}

    public void draw(SpriteBatch batch) {
        if (sprite != null) sprite.draw(batch);
    }

    public void dispose() {
        sprite.dispose();
    }

    public String toString() {
        return "ENTITY";
    }
    public boolean equals(Entity other) {
        return this == other;
    }
}
