package ceat.game.titleScreenGui;

import ceat.game.Font;
import ceat.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class TitleScreen {
    private Game backgroundGame;
    private SpriteBatch batch;
    private float lifetime;
    private BitmapFont font;

    private Texture bgTex;
    private Sprite bgSprite;
    private FloorDialog floorDialog;
    public TitleScreen(SpriteBatch newBatch) {
        batch = newBatch;
        backgroundGame = new Game(batch, 10, false, false);

        font = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 20;
            }
        });

        bgTex = new Texture("img/square.png");
        bgSprite = new Sprite(bgTex);

        bgSprite.setScale(800, 600);
        bgSprite.setPosition(400, 300);
        bgSprite.setColor(0, 0, 0, 0.4f);

        floorDialog = new FloorDialog();
    }

    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        lifetime += delta;
        backgroundGame.render();

        batch.begin();
        bgSprite.draw(batch);
        font.draw(batch, "SUPERBADGUYDESSTROY2200++ ULTRA DELUXE\nEDITION", 15, 400);
        floorDialog.draw(batch);
        batch.end();
    }

    public void keyDown(int keycode) {
        if (floorDialog != null) floorDialog.keyDown(keycode);
    }

    public void keyUp(int keycode) {
        if (floorDialog != null) floorDialog.keyUp(keycode);
    }
    public void dispose() {

    }
}
