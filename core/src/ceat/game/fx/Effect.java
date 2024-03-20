package ceat.game.fx;

import ceat.game.TexSprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Effect {
    private static final ArrayList<Effect> effects = new ArrayList<>();
    private static final Comparator<Effect> compareZIndex = Comparator.comparingInt(o -> o.zIndex);
    private static void sortEffectOrder() {
        Collections.sort(effects, compareZIndex);
    }
    public static void renderEffects(SpriteBatch batch) {
        if (effects.isEmpty()) return;
        boolean isOrdered = true;
        int lastZIndex = effects.get(0).zIndex;
        for (Effect effect: effects) {
            effect.render();
            if (isOrdered && effect.zIndex < lastZIndex) isOrdered = false;
            lastZIndex = effect.zIndex;
        }
        if (!isOrdered) sortEffectOrder();
        for (Effect effect: effects) {
            effect.draw(batch);
        }
    }

    private TexSprite sprite;
    private int zIndex;
    public Effect() {}
    public TexSprite loadSprite(String path) {
        sprite = new TexSprite(path);
        return sprite;
    }
    public TexSprite getSprite() {
        return sprite;
    }
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
    public int getZIndex() {
        return zIndex;
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
        if (sprite != null) sprite.dispose();
    }
}
