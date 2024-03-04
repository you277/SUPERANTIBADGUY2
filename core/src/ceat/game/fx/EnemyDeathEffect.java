package ceat.game.fx;

import ceat.game.ChainedTask;
import ceat.game.Game;
import ceat.game.Loop;
import ceat.game.entity.Enemy;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

public class EnemyDeathEffect extends Effect {
    private final float x;
    private final float y;
    private int frame;
    public EnemyDeathEffect(Game game, Enemy enemy) {
        super(game);
        loadSprite("img/enemyDeathSheet.png");
        sprite.setScale(3f, 3f);
        sprite.setCenter(sprite.getWidth()/2, 0);
        x = enemy.x;
        y = enemy.y;
    }

    public void draw(SpriteBatch batch) {
        System.out.println("draw");
        batch.draw(tex, x + ScreenOffset.offsetY, y + ScreenOffset.offsetY, frame*15, 0);
        //sprite.draw(batch, x + ScreenOffset.offsetY, y + ScreenOffset.offsetY);
    }

    public void play() {
        registerEffect();
        new Loop(0.3f) {
            public void run(float delta, float elapsed) {
                frame = (int)(elapsed/0.3f*8); // 8 frames
            }
        };
        new ChainedTask().wait(0.3f).run(new Timer.Task() {
            @Override
            public void run() {
                unregisterEffect();
                dispose();
            }
        });
    }
}
