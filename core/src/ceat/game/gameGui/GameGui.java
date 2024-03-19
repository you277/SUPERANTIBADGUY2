package ceat.game.gameGui;

import ceat.game.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameGui {
    public static GameGui currentGui;
    private final Game game;
    public boolean enabled;
    public boolean deathScreenEnabled;

    public final EnemyCounter enemyCounter;
    public final CooldownBarList cooldownBarList;
    public final StatusText turnText;
    public final DeathScreen deathScreen;

    public GameGui(Game game) {
        currentGui = this;
        enabled = true;
        this.game = game;
        enemyCounter = new EnemyCounter(game);
        cooldownBarList = new CooldownBarList(game);
        turnText = new StatusText(game);
        deathScreen = new DeathScreen();
    }

    public void draw(SpriteBatch batch) {
        if (!enabled) return;
        enemyCounter.draw(batch);
        cooldownBarList.draw(batch);
        turnText.draw(batch);
        if (deathScreenEnabled)
            deathScreen.draw(batch);
    }

    public void dispose() {
        enemyCounter.dispose();
        cooldownBarList.dispose();
        turnText.dispose();
        deathScreen.dispose();
    }
}
