package ceat.game.gameGui;

import ceat.game.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameGui {
    public static GameGui currentGui;
    private final Game game;
    public boolean enabled;

    public final EnemyCounter enemyCounter;
    public CooldownBarList cooldownBarList;
    public final StatusText turnText;

    public GameGui(Game game) {
        currentGui = this;
        enabled = true;
        this.game = game;
        enemyCounter = new EnemyCounter(game);
        cooldownBarList = new CooldownBarList(game);
        turnText = new StatusText(game);
    }

    public void draw(SpriteBatch batch) {
        if (!enabled) return;
        enemyCounter.draw(batch);
        cooldownBarList.draw(batch);
        turnText.draw(batch);
    }

    public void dispose() {
        enemyCounter.dispose();
        cooldownBarList.dispose();
        turnText.dispose();
    }
}
