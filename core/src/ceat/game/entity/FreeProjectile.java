package ceat.game.entity;

import ceat.game.*;
import ceat.game.entity.enemy.Enemy;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class FreeProjectile extends Entity {
    public enum ProjectileType {
        SENTRY,
        WALL
    }
    private final Vector2 position;
    private float xVelocity;
    private float yVelocity;
    private float lifetime;
    private Enemy parent;
    private boolean alive = true;
    private boolean active = true;
    private final TexSprite sprite;
    private ProjectileType type;

    public FreeProjectile(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        sprite = loadSprite("img/square.png");
        sprite.setScale(5, 5);
        sprite.setCenter();
        position = new Vector2();
        type = ProjectileType.SENTRY;
    }
    public FreeProjectile(Game newGame, Grid newGrid, Enemy parent) {
        this(newGame, newGrid);
        this.parent = parent;
    }
    public FreeProjectile setType(ProjectileType type) {
        this.type = type;
        return this;
    }
    public ProjectileType getType() {
        return type;
    }

    public FreeProjectile setPosition(Vector2 position) {
        this.position.set(position);
        return this;
    }

    public FreeProjectile setPosition(float x, float y) {
        this.position.set(x, y);
        return this;
    }

    public FreeProjectile setVelocity(float xVelocity, float yVelocity) {
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        return this;
    }

    public float getXVelocity() {
        return xVelocity;
    }
    public float getYVelocity() {
        return yVelocity;
    }

    public Enemy getParent() {
        return parent;
    }

    public void doFadeAnimation() {
        new Loop(0.5f) {
            public void run(float delta, float elapsed) {
                float extraScale = elapsed*5;
                sprite.setColor(1, 1, 1, 1 - elapsed*2);
                sprite.setScale(5 + extraScale, 5 + extraScale);
                sprite.setCenter();
            }
            public void onEnd() {
                active = false;
            }
        };
    }

    public boolean getAlive() {
        return alive;
    }

    public boolean getActive() {
        return active;
    }

    public void kill() {
        if (!alive) return;
        alive = false;
        doFadeAnimation();
    }
    public Vector2 getPosition() {
        return position;
    }

    public void render() {
        if (alive) {
            if (lifetime > 5) {
                kill();
            }
        }
        float delta = GameHandler.getDeltaTime();
        lifetime += delta;
        position.set(position.x + xVelocity*delta, position.y + yVelocity*delta);
        sprite.setPosition(ScreenOffset.project(position));
    }
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public String toString() {
        return "FREE PROJECTILE";
    }
    public boolean equals(FreeProjectile other) {
        return xVelocity == other.xVelocity && yVelocity == other.yVelocity && alive == other.alive && active == other.active;
    }
}
