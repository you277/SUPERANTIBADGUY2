package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.Lerp;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class CooldownBar {
    private static final BitmapFont font = Font.create(new Font.ParamSetter() {
        public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
            params.size = 10;
        }
    });
    private static final int barWidth = 40;
    private static final int barHeight = 2;
    private static final int barGap = 40;
    private static final int padding = 10;
    private final Texture bgTex;
    private final Sprite bgSprite;
    private final Texture barTex;
    private final Sprite barSprite;
    private final int index;
    private final String indexStr;

    private float x;
    private float y;
    private float progress;
    private float renderProgress;
    public CooldownBar(int index) {
        bgTex = new Texture("img/square.png");
        barTex = new Texture("img/square.png");
        bgSprite = new Sprite(bgTex);
        barSprite = new Sprite(barTex);
        this.index = index;
        indexStr = "" + index;

        bgSprite.setScale(barWidth, barHeight);
        bgSprite.setColor(1, 1, 1, 0.25f);
        barSprite.setScale(0, barHeight);

        x = 800 - padding - (barWidth + barGap)*index;
        y = padding;
    }

    public void draw(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();
        renderProgress = Lerp.lerp(renderProgress, progress, Lerp.alpha(delta, 15));

        barSprite.setScale(renderProgress*barWidth, barHeight);

        bgSprite.setPosition(x, y);
        barSprite.setPosition(x, y);
        bgSprite.draw(batch);
        barSprite.draw(batch);
        font.draw(batch, indexStr, x, y + barHeight + 5);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void dispose() {
        bgTex.dispose();
        barTex.dispose();
    }
}
