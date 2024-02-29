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
    }
}
