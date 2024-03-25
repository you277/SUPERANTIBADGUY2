package ceat.game.gameGui;

import ceat.game.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameGui {
    public static GameGui currentGui;
    private boolean enabled;
    private boolean deathScreenEnabled;

    private final EnemyCounter enemyCounter;
    private final CooldownBarList cooldownBarList;
    private final StatusText statusText;
    private final FreeProjectileWarning freeProjectileWarning;
    private final DeathScreen deathScreen;

    public GameGui(Game game) {
        currentGui = this;
        enabled = true;
        enemyCounter = new EnemyCounter();
        cooldownBarList = new CooldownBarList();
        statusText = new StatusText(game);
        freeProjectileWarning = new FreeProjectileWarning(game);
        deathScreen = new DeathScreen();
    }

    public EnemyCounter getEnemyCounter() {
        return enemyCounter;
    }
    public CooldownBarList getCooldownBarList() {
        return cooldownBarList;
    }
    public StatusText getStatusText() {
        return statusText;
    }
    public DeathScreen getDeathScreen() {
        return deathScreen;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public void setDeathScreenEnabled(boolean enabled) {
        deathScreenEnabled = enabled;
    }

    public void draw(SpriteBatch batch) {
        if (!enabled) return;
        enemyCounter.draw(batch);
        cooldownBarList.draw(batch);
        freeProjectileWarning.draw(batch);
        statusText.draw(batch);
        if (deathScreenEnabled)
            deathScreen.draw(batch);
    }

    public void dispose() {
        enemyCounter.dispose();
        cooldownBarList.dispose();
        statusText.dispose();
        freeProjectileWarning.dispose();
        deathScreen.dispose();
    }

    public String toString() {
        return "GAME GUI";
    }
    public boolean equals(GameGui other) {
        return this == other;
    }
}
