package ceat.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EmptyTile extends Entity {
    public final entityType type = entityType.EMPTY;
    public float x;
    public float y;

    public EmptyTile(Grid newGrid) {
        super(newGrid);
        super.loadSprite("img/baseTile.png");
        sprite.scale(1.5f);
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.setPosition(x, y);
        super.draw(batch);
    }

    @Override
    public void dispose() {
        tex.dispose();
    }
}
