package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.GameHandler;
import ceat.game.TexSprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class EnemyCounter {
    private int alive;
    private int total;
    private final BitmapFont font;
    private final TexSprite sprite;
    private float lifetime;
    public EnemyCounter() {
        font = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontParameter params) {
                params.size = 20;
            }
        });
        sprite = new TexSprite("img/baseTile.png");

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
        lifetime += GameHandler.getDeltaTime();
        font.draw(batch, alive + "/" + total, 20 + sprite.getWidth() + 20, (float)Math.sin(lifetime)*10 + 10 + 20);
        sprite.setPosition(20, (float)Math.sin(lifetime)*10 + 10 + 20 - sprite.getHeight());
        sprite.draw(batch);
    }
    public void dispose() {
        sprite.dispose();
        font.dispose();
    }

    public String toString() {
        return "ENEMY COUNTER";
    }
    public boolean equals(EnemyCounter other) {
        return this == other;
    }
}
