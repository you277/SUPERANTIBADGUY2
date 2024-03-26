package ceat.game.fx;

import ceat.game.ChainedTask;
import ceat.game.TexSprite;
import ceat.game.entity.enemy.Enemy;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

public class EnemyDeathEffect extends Effect {
    private final Vector2 position;
    private int frame;
    public EnemyDeathEffect(Enemy enemy) {
        position = enemy.getScreenPosition();
        TexSprite sprite = loadSprite("img/enemyDeathSheet.png");
        sprite.setScale(3, 3);
//        sprite.setRegion(0f, 15f, 0, 15);
        sprite.setCenter();
        sprite.setPosition(position);
    }

    public void play() {
        registerEffect();
        TexSprite sprite = getSprite();
        ChainedTask chain = new ChainedTask();
        for (int i = 0; i < 7; i++) {
            int i2 = i;
            chain.run(new Timer.Task() {
                public void run() {
                    System.out.println(i2);
                    frame = i2;
//                    sprite.setRegion( i2*15f, i2*15f + 15, 0, 15);
                    sprite.setPosition(ScreenOffset.project(position));
                    sprite.setCenter();
                }
            }).wait(0.4f);
        }
        chain.run(new Timer.Task() {
            public void run() {
                unregisterEffect();
                dispose();
            }
        });
    }
//    public void render() {
//        TexSprite sprite = getSprite();
//        sprite.setre
//        sprite.setRegion(frame*15f, 0f, frame*15 + 15, 15);
//        sprite.setPosition(ScreenOffset.project(position));
//        sprite.setCenter();
//    }

    public String toString() {
        return "ENEMY DEATH EFFECT";
    }
    public boolean equals(EnemyDeathEffect other) {
        return this == other;
    }
}
