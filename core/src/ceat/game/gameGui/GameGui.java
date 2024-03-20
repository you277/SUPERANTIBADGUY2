package ceat.game.gameGui;

import ceat.game.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameGui {
    private static GameGui currentGui;
    private final Game game;
    public boolean enabled;
    public boolean deathScreenEnabled;

    private final EnemyCounter enemyCounter;
    private final CooldownBarList cooldownBarList;
    private final StatusText statusText;
    private final DeathScreen deathScreen;

    public GameGui(Game game) {
        currentGui = this;
        enabled = true;
        this.game = game;
        enemyCounter = new EnemyCounter(game);
        cooldownBarList = new CooldownBarList(game);
        statusText = new StatusText(game);
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

    public void draw(SpriteBatch batch) {
        if (!enabled) return;
        enemyCounter.draw(batch);
        cooldownBarList.draw(batch);
        statusText.draw(batch);
        if (deathScreenEnabled)
            deathScreen.draw(batch);
    }

    public void dispose() {
        enemyCounter.dispose();
        cooldownBarList.dispose();
        statusText.dispose();
        deathScreen.dispose();
    }
}
