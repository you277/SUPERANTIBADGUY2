package ceat.game.fx;

import ceat.game.Loop;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SelectionParticles extends Effect {
    private float x;
    private float y;
    float xVelocity = 0;
    private float rotation;
    private float rotSpeed;
    public SelectionParticles(float x, float y) {
        rotation = (float)Math.random()*360;
        rotSpeed = -90 + (float)Math.random()*180;

        loadSprite("img/square.png");
        sprite.setScale(3, 3);
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setRotation(rotation);
        this.x = x;
        this.y = y;
    }

    public void play() {
        registerEffect();
        new Loop(0.2f) {
            public void run(float delta, float elapsed) {
                rotation += rotSpeed*delta;
                xVelocity += 100*delta;
                x += xVelocity*delta;
                sprite.setPosition(x, y);
                sprite.setRotation(rotation);
                sprite.setColor(1, 1, 1, 1 - elapsed/0.2f);
            }
            public void onEnd() {
                unregisterEffect();
                dispose();
            }
        };
    }
}
