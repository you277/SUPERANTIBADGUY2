package ceat.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class EmptyTile extends Entity {
    public final entityType type = entityType.EMPTY;

    public EmptyTile() {
        super.loadSprite("ing/what.png");
    }

    @Override
    public void dispose() {
        tex.dispose();
    }
}
