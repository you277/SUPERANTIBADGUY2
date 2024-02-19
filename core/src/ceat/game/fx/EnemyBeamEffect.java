package ceat.game.fx;

import ceat.game.*;
import com.badlogic.gdx.utils.Timer;

public class EnemyBeamEffect extends Effect {
    private final EmptyTile parentTile;
    private float xScale = 1;
    private float yScale = 1;

    public EnemyBeamEffect(Game game, Enemy enemy) {
        super(game);
        parentTile = enemy.parentTile;

        super.loadSprite("img/beam.png");
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight());
    }

    public EnemyBeamEffect setColor(float r, float g, float b) {
        sprite.setColor(r, g, b, 1);
        return this;
    }

    public EnemyBeamEffect setScale(float x, float y) {
        xScale = x;
        yScale = y;
        sprite.setScale(x, y);
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
        return this;
    }

    @Override
    public void play() {
        registerEffect();
        new Loop(0.2f) {
            @Override
            public void run(float deltaTime, float elapsed) {
                sprite.setScale(xScale*((0.2f - elapsed)/0.2f), yScale);
                sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
            }
        };
        new ChainedTask().wait(0.2f).run(new Timer.Task() {
            @Override
            public void run() {
                unregisterEffect();
                tex.dispose();
            }
        });
    }

    @Override
    public void render() {
        sprite.setPosition(parentTile.x, parentTile.y);
    }
}
