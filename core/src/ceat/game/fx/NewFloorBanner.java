package ceat.game.fx;

import ceat.game.ChainedTask;
import ceat.game.Game;
import ceat.game.GameHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

public class NewFloorBanner extends Effect {
    private final BitmapFont[] fonts = new BitmapFont[5];
    private BitmapFont currentFont;
    private final String text;

    public NewFloorBanner(Game game, int floor) {
        super(game);
        text = "FLOOR " + floor;

        FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ARCADE_N.ttf"));

        FreeTypeFontParameter params = new FreeTypeFontParameter();

        params.size = 150;
        fonts[0] = fontGen.generateFont(params);
        params.size = 125;
        fonts[1] = fontGen.generateFont(params);
        params.size = 100;
        fonts[2] = fontGen.generateFont(params);
        params.size = 75;
        fonts[3] = fontGen.generateFont(params);
        params.size = 50;
        fonts[4] = fontGen.generateFont(params);
        fontGen.dispose();

        currentFont = fonts[0];
    }

    @Override
    public void play() {
        registerEffect();
        ChainedTask chain = new ChainedTask();
        for (int i = 1; i < 5; i++) {
            int hi = i;
            chain.wait(0.04f).run(new Timer.Task() {
                @Override
                public void run() {
                    currentFont = fonts[hi];
                }
            });
        }
        chain.wait(1f);
        for (int i = 4; i >= 2; i--) {
            int hi = i;
            chain.run(new Timer.Task() {
                @Override
                public void run() {
                    currentFont = fonts[hi];
                }
            }).wait(0.04f);
        }
        chain.run(new Timer.Task() {
            @Override
            public void run() {
                unregisterEffect();
                for (BitmapFont font: fonts)
                    font.dispose();
            }
        });
    }

    @Override
    public void render() {

    }

    @Override
    public void draw(SpriteBatch batch) {
        currentFont.draw(batch, text, 50, 300);
    }
}
