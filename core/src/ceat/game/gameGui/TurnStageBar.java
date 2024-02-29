package ceat.game.gameGui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TurnStageBar {
    private static int numBars = 4 ;
    private static int barWidth = 40;
    private static int barHeight = 10;
    private static int barGap = 10;
    private static int[][] colors = {
            {1, 1, 1},
            {1, 1, 0},
            {1, 0, 0},
            {1, 0, 0},
    };

    private class Bar {
        private Texture tex;
        private Sprite sprite;
        public Bar() {
            tex = new Texture("assets/img/square.png");
            sprite = new Sprite(tex);
            sprite.setScale(barWidth, barHeight);
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

        public Bar highlight() {
            a = 1;
            sprite.setColor(r, g, b, a);
            return this;
        }
        public Bar unhighlight() {
            a = 0.3f;
            sprite.setColor(r, g, b, a);
            return this;
        }

        public Bar setPosition(float x, float y) {
            sprite.setPosition(x, y);
            return this;
        }

        public void draw(SpriteBatch batch) {
            sprite.draw(batch);
        }

        public void dispose() {
            tex.dispose();
        }
    }

    private Bar[] bars;
    private int currentBar;

    public TurnStageBar() {
        currentBar = 0;
        bars = new Bar[numBars];
        for (int i = 0; i < numBars; i++) {
            int[] color = colors[i];
            bars[i] = new Bar().setColor(color[0], color[1], color[2]).setPosition(400 + i * (barWidth + barGap) - (barWidth + barGap) * ((float) numBars / 2) + (float)barWidth/2, 50).unhighlight();
        }
    }

    public void highlight(int i) {
        bars[currentBar].unhighlight();
        bars[i].highlight();
        currentBar = i;
    }

    public void draw(SpriteBatch batch) {
        for (Bar bar: bars)
            bar.draw(batch);
    }

    public void dispose() {
        for (Bar bar: bars)
            bar.dispose();
    }
}
