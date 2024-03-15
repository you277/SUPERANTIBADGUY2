package ceat.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TexSprite extends Sprite {
    public TexSprite(String path) {
        super(new Texture(path));
    }

    public void setCenter() {
        setCenter(getWidth()/2, getHeight()/2);
    }

    public void dispose() {
        getTexture().dispose();
    }
}
