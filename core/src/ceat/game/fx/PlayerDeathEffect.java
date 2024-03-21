package ceat.game.fx;

import ceat.game.ChainedTask;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

public class PlayerDeathEffect {
    private final float x;
    private final float y;
    public PlayerDeathEffect(Vector2 position) {
        x = position.x;
        y = position.y;
    }

    public void play() {
        ChainedTask chain = new ChainedTask();
        for (int i = 0; i < 5; i++) {
            chain.run(new Timer.Task() {
                public void run() {
                    new PlayerDeathRing(x, y).play();
                }
            }).wait(0.1f);
        }
    }

    public String toString() {
        return "PLAYER DEATH EFFECT";
    }
    public boolean equals(PlayerDeathEffect other) {
        return this == other;
    }
}
