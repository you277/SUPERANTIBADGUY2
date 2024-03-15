package ceat.game.entity.enemy;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.entity.FreeProjectile;
import com.badlogic.gdx.Gdx;

public class SentryEnemy extends Enemy {
    private float timeSinceLastSpawn = 0;
    public SentryEnemy(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        sprite.setColor(0, 1, 0, 1);
    }

    public void animateEntry() {
        animateEntry(0, 1, 0);
    }

    private void spawnProjectiles() {
        if (!grid.active) return;
        if (Math.random() < 0.3) return;
        float baseAngle = (float)(Math.random()*6.28);
        for (int i = 0; i < 8; i++) {
            float angle = baseAngle + i*(float)(6.28/8);
            FreeProjectile proj = new FreeProjectile(game, grid, this).setPosition(x, y).setVelocity(
                    (float)Math.cos(angle)*45,
                    (float)Math.sin(angle)*45
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
