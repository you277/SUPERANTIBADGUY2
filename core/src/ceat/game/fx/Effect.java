package ceat.game.fx;

import ceat.game.Game;
import ceat.game.TexSprite;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class Effect {
    private static ArrayList<Effect> effects = new ArrayList<>();
    public static void renderEffects(SpriteBatch batch) {
        for (Effect effect: effects) {
            effect.render();
            effect.draw(batch);
        }
    }

    public TexSprite sprite;
    public Game game;
    public Effect() {}
    public void loadSprite(String path) {
        sprite = new TexSprite(path);
    }
    public void registerEffect() {
        effects.add(this);
    }
    public void unregisterEffect() {
        effects.remove(this);
    }
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
    public void play() {}
    public void render() {}
    public void dispose() {
        sprite.dispose();
    }
}
