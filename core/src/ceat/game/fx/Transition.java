package ceat.game.fx;

import ceat.game.Loop;
import ceat.game.TexSprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Transition {
    public static class In extends Effect {
        private float rotation;
        public In() {
            loadSprite("img/square.png");
            sprite.setColor(0, 0, 0, 1);
            rotation = (float)Math.random()*360;
            zIndex = 5;
        }

        public void play() {
            registerEffect();
            new Loop(Loop.loopType.UNSYNCED,0.5f) {
                public void run(float delta, float elapsed) {
                    float progress = elapsed/0.5f;
                    rotation += delta*360;
                    sprite.setScale(progress*1000, progress*1000);
                    sprite.setCenter();
                    sprite.setPosition(400, 250);
                    sprite.setRotation(rotation);
                }

                public void onEnd() {
                    onFinish();
                    unregisterEffect();
                }
            };
        }

        public void onFinish() {}
    }
    public static class Out extends Effect {
        private final TexSprite[] sprites;
        private float rotation;
        public Out() {
            sprites = new TexSprite[4];
            for (int i = 0; i < 4; i++) {
                TexSprite sprite = new TexSprite("img/square.png");
                sprite.setColor(0, 0, 0, 1);
                sprite.setScale(900, 900);
                sprite.setCenter();
                sprites[i] = sprite;
            }
            rotation = (float)Math.random()*360;
            zIndex = 5;
        }

        public void play() {
            registerEffect();
            new Loop(Loop.loopType.UNSYNCED,1f) {
                public void run(float delta, float elapsed) {
                    rotation += delta*360;
                    float distance = 450 + elapsed*450;
                    for (int i = 0; i < 4; i++) {
                        float rot = rotation + i*90;
                        float rad = (float)Math.toRadians(rot);
                        TexSprite sprite = sprites[i];
                        sprite.setPosition(400 + (float)Math.cos(rad)*distance, 250 + (float)Math.sin(rad)*distance);
                        sprite.setRotation(rotation);
                    }
                }

                public void onEnd() {
                    unregisterEffect();
                }
            };
        }

        public void draw(SpriteBatch batch) {
            for (TexSprite sprite: sprites)
                sprite.draw(batch);
        }

        public void dispose() {
            for (TexSprite sprite: sprites)
                sprite.dispose();
        }
    }
}
