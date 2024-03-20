package ceat.game.entity.enemy;

import ceat.game.Game;
import ceat.game.GameHandler;
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
        if (!getGrid().getIsActive()) return;
        if (Math.random() < 0.3) return;
        float baseAngle = (float)(Math.random()*6.28);
        for (int i = 0; i < 3; i++) {
            float angle = baseAngle + i*(float)(6.28/3);
            FreeProjectile proj = new FreeProjectile(getGame(), getGrid(), this).setPosition(getScreenPosition()).setVelocity(
                    (float)Math.cos(angle)*45,
                    (float)Math.sin(angle)*45
            );
            getGrid().getFreeProjectiles().add(proj);
        }
    }

    public void render() {
        float delta = GameHandler.getDeltaTime();
        timeSinceLastSpawn += delta;
        if (timeSinceLastSpawn > 5) {
            timeSinceLastSpawn = 0;
            spawnProjectiles();
        }
        super.render();
    }

    public void step() {}

    public String toString() {
        return "GREEN ENEMY";
    }
}
