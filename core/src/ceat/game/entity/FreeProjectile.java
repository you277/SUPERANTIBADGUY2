package ceat.game.entity;

import ceat.game.Game;
import ceat.game.GameHandler;
import ceat.game.Grid;
import ceat.game.Loop;
import ceat.game.entity.enemy.Enemy;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FreeProjectile extends Entity {
    private float x;
    private float y;
    private float xVelocity;
    private float yVelocity;
    private float lifetime;
    public final Enemy parent;
    public boolean alive = true;
    public boolean active = true;
    public FreeProjectile(Game newGame, Grid newGrid, Enemy parent) {
        super(newGame, newGrid);
        super.loadSprite("img/square.png");
        sprite.setScale(5, 5);
        sprite.setCenter();
        this.parent = parent;
    }

    public FreeProjectile setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public FreeProjectile setVelocity(float xVelocity, float yVelocity) {
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        return this;
    }

    public void doFadeAnimation() {
        new Loop(0.5f) {
            public void run(float delta, float elapsed) {
                float extraScale = elapsed*5;
                sprite.setColor(1, 1, 1, 1 - elapsed*2);
                sprite.setScale(5 + extraScale, 5 + extraScale);
                sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
            }
            public void onEnd() {
                active = false;
            }
        };
    }

    public void kill() {
        if (!alive) return;
        alive = false;
        doFadeAnimation();
    }

    public void render() {
        if (alive) {
            if (lifetime > 5) {
                kill();
            }
        }
        float delta = GameHandler.getDeltaTime();
        lifetime += delta;
        x += xVelocity*delta;
        y += yVelocity*delta;
        sprite.setPosition(x + ScreenOffset.offsetX, y + ScreenOffset.offsetY);
    }
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
