package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.Loop;
import ceat.game.fx.SkyBeam;
import com.badlogic.gdx.Gdx;

public class SentryEnemy extends Enemy {
    private float timeSinceLastSpawn = 0;
    public SentryEnemy(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        sprite.setColor(0, 1, 0, 1);
    }

    public void animateEntry() {
        new SkyBeam(game, this)
                .setColor(0, 1, 0)
                .setScale(13, 150).play();
        isAnimating = true;
        new Loop(0.2f) {
            public void run(float delta, float elapsed) {
                x = parentTile.x;
                y = parentTile.y + 400 - (elapsed / 0.2f) * 400;
            }

            public void onEnd() {
                isAnimating = false;
            }
        };
    }

    private void spawnProjectiles() {
        if (Math.random() < 0.3) return;
        float baseAngle = (float)(Math.random()*6.28);
        for (int i = 0; i < 8; i++) {
            float angle = baseAngle + i*(float)(6.28/8);
            FreeProjectile proj = new FreeProjectile(game, grid).setPosition(x, y).setVelocity(
                    (float)Math.cos(angle)*15,
                    (float)Math.sin(angle)*15
            );
            grid.freeProjectiles.add(proj);
        }
    }

    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        timeSinceLastSpawn += delta;
        if (timeSinceLastSpawn > 5) {
            timeSinceLastSpawn = 0;
            spawnProjectiles();
        }
        super.render();
    }

    public void step() {}
}
