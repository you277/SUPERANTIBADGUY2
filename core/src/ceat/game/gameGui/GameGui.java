package ceat.game.gameGui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameGui {
    public static GameGui currentGui;

    public final TurnStageBar stageBar;
    public boolean stageBarEnabled;

    public GameGui() {
        currentGui = this;
        stageBar = new TurnStageBar();
        stageBarEnabled = false;
    }

    public void draw(SpriteBatch batch) {
        if (stageBarEnabled) stageBar.draw(batch);
    }
}
