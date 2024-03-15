package ceat.game.fx;

import ceat.game.Loop;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SelectionParticles extends Effect {
    private float x;
    private float y;
    float xVelocity = 0;
    public SelectionParticles(float x, float y) {
        loadSprite("img/square.png");
        sprite.setScale(3, 3);
        sprite.setCenter();
        sprite.setPosition(x, y);
        this.x = x;
        this.y = y;
    }

    public void play() {
        registerEffect();
        new Loop(0.2f) {
            public void run(float delta, float elapsed) {
                xVelocity = 300;
                x += xVelocity*delta;
                sprite.setPosition(x, y);
                sprite.setColor(1, 1, 1, 1 - elapsed/0.2f);
            }
            public void onEnd() {
                unregisterEffect();
                dispose();
            }
        };
    }
}
