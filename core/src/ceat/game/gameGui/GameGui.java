package ceat.game.gameGui;

import ceat.game.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameGui {
    public static GameGui currentGui;
    private Game game;

    public EnemyCounter enemyCounter;
//    public CardHolder cardHolder;
    public TurnText turnText;

    public GameGui(Game game) {
        currentGui = this;
        this.game = game;
        enemyCounter = new EnemyCounter(game);
//        cardHolder = new CardHolder(game);
        turnText = new TurnText(game);
    }

    public void draw(SpriteBatch batch) {
        enemyCounter.draw(batch);
//        cardHolder.draw(batch);
        turnText.draw(batch);
    }

    public void dispose() {
        enemyCounter.dispose();
//        cardHolder.dispose();
        turnText.dispose();
    }
}
