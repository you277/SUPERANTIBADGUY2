package ceat.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Entity {
    public final entityType type = entityType.PLAYER;
    private moveDirection direction;

    public Player() {
        super.loadSprite("ing/what.png");
    }

    public moveDirection getDirection() {
        return direction;
    }

    public void setDirection(moveDirection newDirection) {
        direction = newDirection;
    }

    @Override
    public void step() {

    }

    @Override
    public void dispose() {
        tex.dispose();
    }
}
