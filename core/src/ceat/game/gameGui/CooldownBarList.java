package ceat.game.gameGui;

import ceat.game.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class CooldownBarList {
    private static final int numBars = 3;
    private final Game game;
    private final ArrayList<CooldownBar> bars = new ArrayList<>();

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
