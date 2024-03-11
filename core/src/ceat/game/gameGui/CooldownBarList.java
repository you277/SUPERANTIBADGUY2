package ceat.game.gameGui;

import ceat.game.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class CooldownBarList {
    private Game game;
    private static int numBars = 3;
    private ArrayList<CooldownBar> bars = new ArrayList<>();
    private CooldownBar currentOpenCard;
    private boolean hasOpenedCard;

    public CooldownBarList(Game game) {
        this.game = game;
        for (int i = 0; i < numBars; i++) {
            CooldownBar bar = new CooldownBar(i);
            bar.setProgress(1);
            bars.add(bar);
        }
    }

    public void draw(SpriteBatch batch) {
        for (CooldownBar bar: bars)
            bar.draw(batch);
    }

    public void setBarProgress(int barIndex, float progress) {
        bars.get(barIndex).setProgress(progress);
    }

    public void dispose() {
        for (CooldownBar bar: bars)
            bar.dispose();
    }
}
