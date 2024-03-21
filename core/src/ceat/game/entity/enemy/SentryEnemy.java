package ceat.game.entity.enemy;

import ceat.game.Game;
import ceat.game.GameHandler;
import ceat.game.Grid;
import ceat.game.entity.FreeProjectile;

public class SentryEnemy extends Enemy {
    private float timeSinceLastSpawn = 0;
    private final Game game;
    private final Grid grid;
    public SentryEnemy(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        getSprite().setColor(0, 1, 0, 1);
        game = getGame();
        grid = getGrid();
    }

    public void animateEntry() {
        animateEntry(0, 1, 0);
    }

    private void spawnProjectiles() {
        if (!grid.getIsActive()) return;
        if (Math.random() < 0.3) return;
        float baseAngle = (float)(Math.random()*6.28);
        for (int i = 0; i < 3; i++) {
            float angle = baseAngle + i*(float)(6.28/3);
            FreeProjectile proj = new FreeProjectile(game, grid, this).setPosition(getScreenPosition()).setVelocity(
                    (float)Math.cos(angle)*45,
                    (float)Math.sin(angle)*45
            );
            grid.getFreeProjectiles().add(proj);
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
    public boolean equals(SentryEnemy other) {
        return this == other;
    }
}
