package ceat.game.fx;

import ceat.game.Loop;
import ceat.game.TexSprite;

public class PlayerDeathRing extends Effect {
    private final float x;
    private final float y;
    public PlayerDeathRing(float x, float y) {
        this.x = x;
        this.y = y;
        TexSprite sprite = loadSprite("img/squareRing.png");
        sprite.setScale(0, 0);
        sprite.setCenter();
        sprite.setPosition(x, y);
    }

    public void play() {
        registerEffect();
        new Loop(1) {
            public void run(float delta, float elapsed) {
                float size = elapsed*500;
                TexSprite sprite = getSprite();
                sprite.setScale(size);
                sprite.setCenter();
                sprite.setPosition(x, y);
            }
            public void onEnd() {
                unregisterEffect();
                dispose();
            }
        };
    }

    public String toString() {
        return "PLAYER DEATH RING";
    }
    public boolean equals(PlayerDeathRing other) {
        return this == other;
    }
}
