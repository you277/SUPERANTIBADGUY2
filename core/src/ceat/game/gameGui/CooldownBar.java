package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.GameHandler;
import ceat.game.Lerp;
import ceat.game.TexSprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class CooldownBar {
    private static final BitmapFont font = Font.create(new Font.ParamSetter() {
        public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
            params.size = 10;
        }
    });
    private static final int barWidth = 80;
    private static final int barHeight = 2;
    private static final int barGap = 5;
    private static final int padding = 10;
    private final TexSprite bgSprite;
    private final TexSprite barSprite;
    private final String indexStr;

    private float x;
    private float y;
    private float progress;
    private float renderProgress;
    private float lifetime;
    public CooldownBar(int index) {
        bgSprite = new TexSprite("img/square.png");
        barSprite = new TexSprite("img/square.png");
        indexStr = "" + (index + 1);

        bgSprite.setScale(barWidth, barHeight);
        bgSprite.setColor(1, 1, 1, 0.25f);
        barSprite.setScale(0, barHeight);

        x = 400 + (barWidth + barGap)*index;
        y = padding;
    }

    public void draw(SpriteBatch batch) {
        float delta = GameHandler.getDeltaTime();
        float a = Lerp.alpha(delta, 15);
        lifetime += delta;
        renderProgress = Lerp.lerp(renderProgress, progress, a);

        barSprite.setScale(renderProgress*barWidth, barHeight);

        if (progress == 1) {
            y = Lerp.lerp(y, padding, a);
        } else {
            y = Lerp.lerp(y, 2 + (float)Math.sin(lifetime)*2, a);
        }

        bgSprite.setPosition(x, y);
        barSprite.setPosition(x - (1 - renderProgress)*barWidth/2, y);
        bgSprite.draw(batch);
        barSprite.draw(batch);
        font.draw(batch, indexStr, x - barWidth/2, y + barHeight + 10 + 2);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void dispose() {
        bgSprite.dispose();
        barSprite.dispose();
    }

    public String toString() {
        return "COOLDOWN BAR " + indexStr;
    }
    public boolean equals(CooldownBar other) {
        return this == other;
    }
}
