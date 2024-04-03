package ceat.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

public class Font {
    public static FreeTypeFontGenerator fontGen;
    private static final GlyphLayout layout = new GlyphLayout();
    public static void createGenerator() {
        fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ARCADE_N.ttf"));
    }
    public static Vector2 getTextSize(BitmapFont font, String text) {
        layout.setText(font, text);
        return new Vector2(layout.width, layout.height);
    }
    public static float getTextWidth(BitmapFont font, String text) {
        layout.setText(font, text);
        return layout.width;
    }
    public static float getTextHeight(BitmapFont font, String text) {
        layout.setText(font, text);
        return layout.height;
    }

    public static class ParamSetter {
        public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {}
    }

    public static BitmapFont create(ParamSetter paramSetter) {
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramSetter.run(params);
        return fontGen.generateFont(params);
    }

    public String toString() {
		return "FONT";
	}
	public boolean equals(Font other) {
		return this == other;
	}
}
