package ceat.game.titleScreenGui;

import ceat.game.Font;
import ceat.game.Game;
import ceat.game.TexSprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class TitleScreen {
    private Game backgroundGame;
    private SpriteBatch batch;
    private float lifetime;
    private BitmapFont font;

    private TexSprite bgSprite;
    private FloorDialog floorDialog;
    private Selection startSelect;
    private Selection floorSelect;
    private int currentSelection;
    public TitleScreen(SpriteBatch newBatch) {
        batch = newBatch;
        backgroundGame = new Game(batch, 10, false, false);

        font = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 20;
            }
        });

        bgSprite = new TexSprite("img/square.png");

        bgSprite.setScale(800, 600);
        bgSprite.setPosition(400, 300);
        bgSprite.setColor(0, 0, 0, 0.4f);

        currentSelection = 0;
        startSelect = new Selection("START FLOOR 1").setPosition(0, 300).setSelected(true);
        floorSelect = new Selection("SELECT FLOOR").setPosition(0, 200).setSelected(false);
    }

    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        lifetime += delta;
        backgroundGame.render();

        batch.begin();
        bgSprite.draw(batch);
        font.draw(batch, "SUPERBADGUYDESSTROY2200++ ULTRA DELUXE\nEDITION", 15, 470);
        startSelect.draw(batch);
        floorSelect.draw(batch);
        if (floorDialog != null)
            floorDialog.draw(batch);
        batch.end();
    }

    public void keyDown(int keycode) {
        if (floorDialog != null) {
            floorDialog.keyDown(keycode);
            return;
        }
        switch (keycode) {
            case Keys.W:
            case Keys.UP:
                currentSelection--;
                break;
            case Keys.S:
            case Keys.DOWN:
                currentSelection++;
                break;
            case Keys.NUM_1:
            case Keys.NUMPAD_1:
                currentSelection = 0;
                break;
            case Keys.NUM_2:
            case Keys.NUMPAD_2:
                currentSelection = 1;
                break;
            case Keys.ENTER:
            case Keys.NUMPAD_ENTER:
                if (currentSelection == 0) {
                    startGame(1);
                } else {
                    floorDialog = new FloorDialog() {
                        public void onConfirm(int floor) {
                            floorDialog.dispose();
                            floorDialog = null;
                            startGame(floor);
                        }
                        public void onCancel() {
                            floorDialog.dispose();
                            floorDialog = null;
                        }
                    };
                }
        }
        if (currentSelection < 0) currentSelection = 1;
        else if (currentSelection > 1) currentSelection = 0;
        if (currentSelection == 0) {
            startSelect.setSelected(true);
            floorSelect.setSelected(false);
        } else {
            startSelect.setSelected(false);
            floorSelect.setSelected(true);
        }
    }

    public void keyUp(int keycode) {
        if (floorDialog != null) floorDialog.keyUp(keycode);
    }
    public void startGame(int floor) {}
    public void dispose() {
        startSelect.dispose();
        floorSelect.dispose();
        bgSprite.dispose();
        font.dispose();
    }
}
