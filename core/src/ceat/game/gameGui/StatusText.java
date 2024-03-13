package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.Game;
import ceat.game.Loop;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class StatusText {
    private final BitmapFont font = Font.create(new Font.ParamSetter() {
        public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
            params.size = 7;
        }
    });

    private final Game game;
    private float yOffset;
    public boolean visible;
    public String text = "";

    public StatusText(Game game) {
        this.game = game;
        visible = true;
    }

    public void shake() {
        new Loop(0.25f) {
            public void run(float delta, float elapsed) {
                float strength = (float)(-1 + Math.random()*2)*10;
                float mult = 1 - elapsed/0.25f;
                yOffset = strength*mult;
            }
            public void onEnd() {
                yOffset = 0;
            }
        };
    }

    public void pop() {
        new Loop(0.15f) {
            public void run(float delta, float elapsed) {
                float mult = 1 - elapsed/0.15f;
                yOffset = mult*5;
            }
            public void onEnd() {
                yOffset = 0;
            }
        };
    }

    public void draw(SpriteBatch batch) {
        if (!visible) return;
        if (text.length() == 0) return;
        font.draw(batch, text,
                game.player.x + game.player.sprite.getWidth()/2 + ScreenOffset.offsetX + 5,
                game.player.y + game.player.sprite.getHeight()/2 + ScreenOffset.offsetY + 20 + yOffset
        );
    }

    public void dispose() {
        font.dispose();
    }
}
