package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.Lerp;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class AbilityCard {
    private static BitmapFont font = Font.create(new Font.ParamSetter() {
        public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
            params.size = 10;
        }
    });
    private Texture tex;
    private Sprite sprite;
    private int index;
    private String name;

    private boolean open;
    private float x;
    private float y;
    private float brightness = 0.15f;
    public AbilityCard(int index, String name) {
        tex = new Texture("img/card.png");
        sprite = new Sprite(tex);
        this.index = index;
        this.name = name;

        sprite.setScale(4, 4);
//        sprite.setCenter(sprite.getWidth(), 0);
        sprite.setColor(brightness, brightness, brightness, 1);

        x = 800 - index * (sprite.getWidth()*4 + 10) - 20 - sprite.getWidth()*4;
    }

    public void draw(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();
        float a = Lerp.alpha(delta, 15);
        if (open) {
            brightness = Lerp.lerp(brightness, 0.3f, a);
            y = Lerp.lerp(y, -15, a);
        } else {
            brightness = Lerp.lerp(brightness, 0.15f, a);
            y = Lerp.lerp(y, -60, a);
        }
        sprite.setColor(brightness, brightness, brightness, 1);
        sprite.setPosition(x, y);
        sprite.draw(batch);
        font.draw(batch, name, x, y + sprite.getHeight() - 10);
    }

    public void open() {
        open = true;
    }

    public void close() {
        open = false;
    }

    public void dispose() {
        tex.dispose();
    }
}
