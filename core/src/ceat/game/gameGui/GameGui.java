package ceat.game.gameGui;

import ceat.game.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameGui {
    public static GameGui currentGui;
    private Game game;

    public EnemyCounter enemyCounter;

    public GameGui(Game game) {
        currentGui = this;
        this.game = game;
        enemyCounter = new EnemyCounter(game);
    }

    public void draw(SpriteBatch batch) {
        enemyCounter.draw(batch);
    }

    public void dispose() {

    }
}
