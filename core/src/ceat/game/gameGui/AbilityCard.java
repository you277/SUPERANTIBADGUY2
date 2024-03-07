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
            params.size = 50;
        }
    });
    private Texture tex;
    private Sprite sprite;
    private int index;
    private String name;

    private boolean open;
    private float x;
    private float y;
    public AbilityCard(int index, String name) {
        tex = new Texture("img/card.png");
        sprite = new Sprite(tex);
        this.index = index;
        this.name = name;

        sprite.setScale(2, 2);
        sprite.setCenter(sprite.getWidth(), 0);

        x = 800 - index * 60 - 10;
    }

    public void draw(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();
        if (open) {
            y = Lerp.lerp(y, 0, Lerp.alpha(delta, 15));
        } else {
            y = Lerp.lerp(y, -20, Lerp.alpha(delta, 15));
        }
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
