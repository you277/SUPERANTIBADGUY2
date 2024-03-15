package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.TexSprite;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class DeathScreen {
    private TexSprite bgSprite;
    private GlyphLayout layout;

    private BitmapFont titleFont;
    private BitmapFont statFont;
    private int enemiesKilled;
    private int enemiesIgnored;
    private int turnsTaken;
    private int shotsFired;
    private int startFloor;
    private int finalFloor;
    private int floorsDone;

    public DeathScreen() {
        bgSprite = new TexSprite("img/square.png");
        bgSprite.setScale(800, 600);
        bgSprite.setColor(0, 0, 0, 0.5f);
        bgSprite.setPosition(400, 300);
        layout = new GlyphLayout();

        titleFont = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 25;
            }
        });
        statFont = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 15;
            }
        });
    }

    public void set(int startFloor, int finalFloor, int floorsDone, int enemiesKilled, int enemiesIgnored, int shotsFired, int turnsTaken) {
        this.startFloor = startFloor;
        this.finalFloor = finalFloor;
        this.floorsDone = floorsDone;
        this.enemiesKilled = enemiesKilled;
        this.enemiesIgnored = enemiesIgnored;
        this.shotsFired = shotsFired;
        this.turnsTaken = turnsTaken;
    }

    public void drawStat(SpriteBatch batch, String name, String value, float height) {
        layout.setText(statFont, name);
        statFont.draw(batch, name, 400 - layout.width - 10, height);
        layout.setText(statFont,  value);
        statFont.draw(batch, value, 400 + 10, height);
    }

    public void draw(SpriteBatch batch) {
        bgSprite.draw(batch);

        layout.setText(titleFont, "YOU ARE DEAD");
        titleFont.draw(batch, "YOU ARE DEAD", 400 - layout.width/2, 400);

        float statsTop = 300;

        drawStat(batch, "FINAL FLOOR", "" + finalFloor, statsTop);
        drawStat(batch, "FLOORS CONQUERED", "" + floorsDone, statsTop - 25);
        drawStat(batch, "ENEMIES KILLED", "" + enemiesKilled, statsTop - 50);
        drawStat(batch, "ENEMIES IGNORED", "" + enemiesIgnored, statsTop - 75);
        drawStat(batch, "SHOTS FIRED", "" + shotsFired, statsTop - 100);
        drawStat(batch, "TURNS TAKEN", "" + turnsTaken, statsTop - 125);

        String hi = "[ESC] TO TITLE - [R] TO RETURN TO FLOOR [" + startFloor + "]";
        layout.setText(statFont, hi);
        statFont.draw(batch, hi, 400 - layout.width/2, 75);
    }
}
