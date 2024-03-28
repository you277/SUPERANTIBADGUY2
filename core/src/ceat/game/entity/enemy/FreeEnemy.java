package ceat.game.entity.enemy;

import ceat.game.ChainedTask;
import ceat.game.Game;
import ceat.game.GameHandler;
import ceat.game.Grid;
import com.badlogic.gdx.utils.Timer;

public class FreeEnemy extends Enemy {
    public FreeEnemy(Game newGame, Grid newGrid) {
        super(newGame, newGrid, EnemyType.FREE);
        getSprite().setColor(0, 1, 1, 1);
    }

    public void animateEntry() {
        animateEntry(0, 1, 1);
    }

    private float lifetime;
    private float lastTurnTime;
    public void render() {
        float delta = GameHandler.getDeltaTime();
        lifetime += delta;
        if (lifetime - lastTurnTime > 1.5f) {
            lastTurnTime = lifetime;
            super.step();
            new ChainedTask().wait(0.25f).run(new Timer.Task() {
                public void run() {
                    Game game = getGame();
                    game.processProjectilesAndEnemies();
                    game.processPlayerAndEnemies();
                    game.postEnemyProcesses();
                }
            });
        }
        super.render();
    }

    public void step() {}

    public String toString() {
        return "CYAN ENEMY";
    }
    public boolean equals(FreeEnemy other) {
        return this == other;
    }
}
