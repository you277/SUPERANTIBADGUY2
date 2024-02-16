package ceat.game.fx;

import ceat.game.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;

public class BeamEffect extends Effect {
    private Texture tex;
    private Sprite sprite;
    private EmptyTile parentTile;
    private float xScale = 1;
    private float yScale = 1;

    public BeamEffect(TheActualGame game, EmptyTile parent) {
        super(game);
        parentTile = parent;

        super.loadSprite("img/beam.png");
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
    }

    public BeamEffect setColor(float r, float g, float b) {
        sprite.setColor(r, g, b, 1);
        return this;
    }

    public BeamEffect setScale(float x, float y) {
        xScale = x;
        yScale = y;
        sprite.setScale(x, y);
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
        return this;
    }

    @Override
    public void play() {
        super.registerEffect();
        new Loop(0.2f) {
            @Override
            public void run(float deltaTime, float elapsed) {
                sprite.setScale(xScale, yScale*((0.2f - elapsed)/0.2f));
                sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
            }
        };
        new ChainedTask().wait(0.2f).run(new Timer.Task() {
            @Override
            public void run() {
                BeamEffect.super.unregisterEffect();
                tex.dispose();
            }
        });
    }

    @Override
    public void render() {
        sprite.setPosition(parentTile.x, parentTile.y);
    }
}
