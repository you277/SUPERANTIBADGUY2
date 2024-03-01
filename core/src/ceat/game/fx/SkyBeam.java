package ceat.game.fx;

import ceat.game.*;
import ceat.game.entity.BoardEntity;
import ceat.game.entity.EmptyTile;
import ceat.game.screen.ScreenOffset;

public class SkyBeam extends Effect {
    private final EmptyTile parentTile;
    private float xScale = 1;
    private float yScale = 1;

    public SkyBeam(Game game, BoardEntity boardEntity) {
        super(game);
        parentTile = boardEntity.parentTile;
        super.loadSprite("img/beam.png");
        sprite.setCenter(sprite.getWidth()/2, 0);
    }

    public SkyBeam(Game game, EmptyTile emptyTile) {
        super(game);
        parentTile = emptyTile;
        super.loadSprite("img/beam.png");
        sprite.setCenter(sprite.getWidth()/2, 0);
    }

    public SkyBeam setColor(float r, float g, float b) {
        sprite.setColor(r, g, b, 1);
        return this;
    }

    public SkyBeam setScale(float x, float y) {
        xScale = x;
        yScale = y;
        sprite.setScale(x, y);
        sprite.setCenter(sprite.getWidth()/2, 0);
        return this;
    }

    public SkyBeam setRotation(float degrees) {
        sprite.setRotation(degrees);
        return this;
    }

    @Override
    public void play() {
        registerEffect();
        new Loop(0.2f) {
            @Override
            public void run(float deltaTime, float elapsed) {
                sprite.setScale(xScale*((0.2f - elapsed)/0.2f), yScale);
                sprite.setCenter(sprite.getWidth()/2, 0);
            }

            @Override
            public void onEnd() {
                unregisterEffect();
                dispose();
            }
        };
    }

    @Override
    public void render() {
        sprite.setPosition(parentTile.x + ScreenOffset.offsetX, parentTile.y + ScreenOffset.offsetY);
    }
}
