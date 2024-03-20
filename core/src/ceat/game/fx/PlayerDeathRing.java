package ceat.game.fx;

import ceat.game.Loop;

public class PlayerDeathRing extends Effect {
    private final float x;
    private final float y;
    public PlayerDeathRing(float x, float y) {
        this.x = x;
        this.y = y;
        loadSprite("img/squareRing.png");
        sprite.setScale(0, 0);
        sprite.setCenter();
        sprite.setPosition(x, y);
    }

    public void play() {
        registerEffect();
        new Loop(1) {
            public void run(float delta, float elapsed) {
                float size = elapsed*500;
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
}
