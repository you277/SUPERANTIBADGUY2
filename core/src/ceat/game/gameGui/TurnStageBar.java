package ceat.game.gameGui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TurnStageBar {
    private class Bar {
        private Texture tex;
        private Sprite sprite;
        public Bar() {
            tex = new Texture("assets/img/square");
            sprite = new Sprite(tex);
            sprite.setScale(15, 5);
            sprite.setCenter(sprite.getWidth()/2, 0);
        }

        private float r;
        private float g;
        private float b;
        private float a;

        public Bar setColor(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
            sprite.setColor(r, g, b, a);
            return this;
        }

        public Bar setOpacity(float a) {
            this.a = a;
            sprite.setColor(r, g, b, a);
            return this;
        }

        public Bar setPosition(float x, float y) {
            sprite.setPosition(x, y);
            return this;
        }
    }
}
