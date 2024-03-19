package ceat.game.titleScreenGui;

import ceat.game.Font;
import ceat.game.GameHandler;
import ceat.game.Lerp;
import ceat.game.TexSprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;

public class FloorDialog {
    private final BitmapFont titleFont;
    private final BitmapFont inputFont;
    private final BitmapFont emptyInputFont;
    private final GlyphLayout layout;
    private final HashMap<Integer, Integer> textMap;
    private int currentInput;
    private float lifetime;
    private float inputYPosition;
    private final TexSprite bgSprite;
    private Sound clickSound;
    public FloorDialog() {
        layout = new GlyphLayout();
        inputYPosition = 200;
        titleFont = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 15;
            }
        });
        inputFont = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 15;
            }
        });
        emptyInputFont = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 15;
                params.color = new Color(1, 1, 1, 0.25f);
            }
        });

        bgSprite = new TexSprite("img/square.png");

        textMap = new HashMap<>();
        textMap.put(Keys.NUM_1, 1);
        textMap.put(Keys.NUM_2, 2);
        textMap.put(Keys.NUM_3, 3);
        textMap.put(Keys.NUM_4, 4);
        textMap.put(Keys.NUM_5, 5);
        textMap.put(Keys.NUM_6, 6);
        textMap.put(Keys.NUM_7, 7);
        textMap.put(Keys.NUM_8, 8);
        textMap.put(Keys.NUM_9, 9);
        textMap.put(Keys.NUM_0, 0);
        textMap.put(Keys.NUMPAD_1, 1);
        textMap.put(Keys.NUMPAD_2, 2);
        textMap.put(Keys.NUMPAD_3, 3);
        textMap.put(Keys.NUMPAD_4, 4);
        textMap.put(Keys.NUMPAD_5, 5);
        textMap.put(Keys.NUMPAD_6, 6);
        textMap.put(Keys.NUMPAD_7, 7);
        textMap.put(Keys.NUMPAD_8, 8);
        textMap.put(Keys.NUMPAD_9, 9);
        textMap.put(Keys.NUMPAD_0, 0);

        clickSound = Gdx.audio.newSound(Gdx.files.internal("snd/click.mp3"));
    }

    private boolean backspaceDown;
    private float backspaceDownTime;

    public void keyDown(int keycode) {
        inputYPosition = 207.5f;
        if (textMap.containsKey(keycode)) {
            clickSound.play(10);
            int nextInput = currentInput*10 + textMap.get(keycode);
            if (nextInput < 0) nextInput = Integer.MAX_VALUE;
            currentInput = nextInput;
            return;
        }
        if (keycode == Keys.BACKSPACE && currentInput > 0) {
            clickSound.play(10);
            currentInput /= 10;
            backspaceDown = true;
            backspaceDownTime = 0;
        } else if (keycode == Keys.ENTER || keycode == Keys.NUMPAD_ENTER) {
            onConfirm(currentInput == 0 ? 1 : currentInput);
        } else if (keycode == Keys.ESCAPE) {
            onCancel();
        }
    }

    public void onConfirm(int finalInput) {}
    public void onCancel() {}

    public void keyUp(int keycode) {
        if (keycode == Keys.BACKSPACE) backspaceDown = false;
    }

    private final String titleText = "INPUT STARTING FLOOR";
    private float bgScale = 800;
    private float bgOpacity = 0;

    public void draw(SpriteBatch batch) {
        float delta = GameHandler.getDeltaTime();
        lifetime += delta;

        float yOffset = (float)Math.sin(lifetime)*10;

        if (backspaceDown) {
            if (backspaceDownTime > 0.25 && currentInput > 0) {
                if (currentInput != 0) {
                    clickSound.play(10);
                }
                currentInput /= 10;
                inputYPosition = 207.5f;
            } else {
                backspaceDownTime += delta;
            }
        }

        float a = Lerp.alpha(delta, 10);
        bgScale = Lerp.lerp(bgScale, 400, a);
        bgOpacity = Lerp.lerp(bgOpacity, 0.5f, a);

        bgSprite.setColor(0, 0, 0, bgOpacity);
        bgSprite.setScale(bgScale, bgScale);
        bgSprite.setCenter(bgSprite.getWidth()/2, bgSprite.getHeight()/2);
        bgSprite.setPosition(400, 250);
        bgSprite.setRotation(lifetime*-90);
        bgSprite.draw(batch);

        boolean empty = currentInput == 0;
        BitmapFont currentInputFont = empty ? emptyInputFont : inputFont;
        String text = empty ? "1" : "" + currentInput;

        layout.setText(titleFont, titleText);
        titleFont.draw(batch, titleText, 400 - layout.width/2, 300 + yOffset);

        layout.setText(currentInputFont, text);
        inputYPosition = Lerp.lerp(inputYPosition, 200, a);
        currentInputFont.draw(batch, text, 400 - layout.width/2, inputYPosition + yOffset);
    }

    public void dispose() {
        bgSprite.dispose();
        titleFont.dispose();
        inputFont.dispose();
        emptyInputFont.dispose();
        clickSound.dispose();
    }
}
