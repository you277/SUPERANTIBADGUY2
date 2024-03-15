package ceat.game.fx;

import ceat.game.ChainedTask;
import ceat.game.Font;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Timer;

public class NewFloorBanner extends Effect {
    private static BitmapFont[] fonts;

    private static boolean fontsCreated = false;
    public static void createFonts() {
        if (fontsCreated) return;
        fontsCreated = true;

        int fontMaxSize = 150;
        int fontMinSize = 50;
        int numFonts = 20;

        int increment = (fontMaxSize-fontMinSize)/numFonts;

        fonts = new BitmapFont[numFonts];
        for (int i = 0; i < numFonts; i++) {
            int i2 = i;
            fonts[i] = Font.create(new Font.ParamSetter() {
                public void run(FreeTypeFontParameter params) {
                    params.size = fontMaxSize - increment*i2;
                }
            });
        }
    }
    private BitmapFont currentFont;
    private final String text;
    private int y = 325;

    public NewFloorBanner(int floor) {
        super();
        text = "FLOOR " + floor;
        currentFont = fonts[0];
    }

    public void play() {
        registerEffect();
        ChainedTask chain = new ChainedTask();
        for (int i = 1; i < fonts.length; i++) {
            int hi = i;
            fonts[hi].setColor(1, 1, 1, 1);
            chain.wait(0.01f).run(new Timer.Task() {
                @Override
                public void run() {
                    currentFont = fonts[hi];
                }
            });
        }
        chain.wait(1f);
        for (int i = fonts.length - 1; i >= 0; i--) {
            int hi = i;
            chain.run(new Timer.Task() {
                @Override
                public void run() {
                    currentFont = fonts[hi];
                    currentFont.setColor(1, 1, 1, (float)hi/fonts.length);
                }
            }).wait(0.01f);
        }
        chain.run(new Timer.Task() {
            @Override
            public void run() {
                unregisterEffect();
                dispose();
            }
        });
    }

    public void draw(SpriteBatch batch) {
        y -= Gdx.graphics.getDeltaTime()*10;
        currentFont.draw(batch, text, 50, y);
    }
}
