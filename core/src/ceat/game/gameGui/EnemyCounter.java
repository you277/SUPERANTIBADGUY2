package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class EnemyCounter {
    private int alive;
    private int total;
    private Game game;
    private BitmapFont font;
    public EnemyCounter(Game game) {
        this.game = game;
        font = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontParameter params) {
                params.size = 20;
            }
        });
    }
    public void setAlive(int alive) {
        this.alive = alive;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    public void draw(SpriteBatch batch) {
        font.draw(batch, alive + "/" + total, 10, (float)Math.sin(game.gameTime)*10 + 10 + 20);
    }
    public void dispose() {
        font.dispose();
    }
}
