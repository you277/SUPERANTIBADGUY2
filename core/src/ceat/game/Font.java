package ceat.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Font {
    public static FreeTypeFontGenerator fontGen;
    public static void createGenerator() {
        fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ARCADE_N.ttf"));
    }

    public static class ParamSetter {
        public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {}
    }

    public static BitmapFont create(ParamSetter paramSetter) {
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramSetter.run(params);
        return fontGen.generateFont(params);
    }
}
