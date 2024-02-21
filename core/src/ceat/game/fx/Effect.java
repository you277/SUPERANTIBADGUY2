package ceat.game.fx;

import ceat.game.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Effect {
    public Texture tex;
    public Sprite sprite;
    public Game game;
    public Effect(Game newGame) {
        game = newGame;
    }
    public void loadSprite(String path) {
        tex = new Texture(path);
        sprite = new Sprite(tex);
    }
    public void registerEffect() {
        game.effects.add(this);
    }
    public void unregisterEffect() {
        game.effects.remove(this);
    }
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
    public void play() {}
    public void render() {}
    public void dispose() {
        tex.dispose();
    }
}
