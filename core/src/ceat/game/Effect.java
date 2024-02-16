package ceat.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Effect {
    public Texture tex;
    public Sprite sprite;
    public TheActualGame game;
    public Effect(TheActualGame newGame) {
        game = newGame;
    }
    public Sprite loadSprite(String path) {
        tex = new Texture(path);
        sprite = new Sprite(tex);
        return sprite;
    }
    public void registerEffect() {
        game.effects.add(this);
    }
    public void unregisterEffect() {
        game.effects.remove(this);
    }
    public void draw(SpriteBatch batch) {

    }
    public void play() {}
    public void render() {}
}
