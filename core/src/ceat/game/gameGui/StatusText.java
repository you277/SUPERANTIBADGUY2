package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.Game;
import ceat.game.Loop;
import ceat.game.entity.Player;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

public class StatusText {
    private final BitmapFont font = Font.create(new Font.ParamSetter() {
        public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
            params.size = 7;
        }
    });

    private final Game game;
    private float yOffset;
    private boolean visible;
    private String text = "";

    public StatusText(Game game) {
        this.game = game;
        visible = true;
    }

    public void shake() {
        new Loop(0.25f) {
            public void run(float delta, float elapsed) {
                float strength = (float)(-1 + Math.random()*2)*10;
                float multiplier = 1 - elapsed/0.25f;
                yOffset = strength*multiplier;
            }
            public void onEnd() {
                yOffset = 0;
            }
        };
    }

    public void pop() {
        new Loop(0.15f) {
            public void run(float delta, float elapsed) {
                float multiplier = 1 - elapsed/0.15f;
                yOffset = multiplier*5;
            }
            public void onEnd() {
                yOffset = 0;
            }
        };
    }

    public void setText() {
        this.text = "";
    }
    public void setText(String text) {
        this.text = text;
    }

    public void draw(SpriteBatch batch) {
        if (!visible) return;
        if (text.isEmpty()) return;
        Player player = game.getPlayer();
        Vector2 position = player.getScreenPosition();
        font.draw(batch, text,
                position.x + player.getSprite().getWidth()/2 + ScreenOffset.offsetX + 5,
                position.y + player.getSprite().getHeight()/2 + ScreenOffset.offsetY + 20 + yOffset
        );
    }

    public void dispose() {
        font.dispose();
    }

    public String toString() {
        return "STATUS TEXT";
    }
    public boolean equals(StatusText other) {
        return this == other;
    }
}
