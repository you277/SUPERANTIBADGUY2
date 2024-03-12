package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class EnemyCounter {
    private int alive;
    private int total;
    private final Game game;
    private final BitmapFont font;
    private Texture tex;
    private Sprite sprite;
    public EnemyCounter(Game game) {
        this.game = game;
        font = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontParameter params) {
                params.size = 20;
            }
        });
        tex = new Texture("img/baseTile.png");
        sprite = new Sprite(tex);

        sprite.setColor(1, 0, 0, 1);
        sprite.setScale(2);
    }
    public void setAlive(int alive) {
        this.alive = alive;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    public void draw(SpriteBatch batch) {
        font.draw(batch, alive + "/" + total, 20 + sprite.getWidth() + 20, (float)Math.sin(game.gameTime)*10 + 10 + 20);
        sprite.setPosition(20, (float)Math.sin(game.gameTime)*10 + 10 + 20 - sprite.getHeight());
        sprite.draw(batch);
    }
    public void dispose() {
        tex.dispose();
        font.dispose();
    }
}
