package ceat.game.titleScreenGui;

import ceat.game.Font;
import ceat.game.Lerp;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public class FloorDialog {
    private BitmapFont titleFont;
    private BitmapFont inputFont;
    private BitmapFont emptyInputFont;
    private String currentInput;
    private GlyphLayout layout;
    private HashMap<Integer, String> textMap;
    private boolean inputFinished;
    private String finalText;
    private float lifetime;
    private float inputYPosition;
    public FloorDialog() {
        currentInput = "";
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
        textMap = new HashMap<>();
        textMap.put(Keys.NUM_1, "1");
        textMap.put(Keys.NUM_2, "2");
        textMap.put(Keys.NUM_3, "3");
        textMap.put(Keys.NUM_4, "4");
        textMap.put(Keys.NUM_5, "5");
        textMap.put(Keys.NUM_6, "6");
        textMap.put(Keys.NUM_7, "7");
        textMap.put(Keys.NUM_8, "8");
        textMap.put(Keys.NUM_9, "9");
        textMap.put(Keys.NUMPAD_0, "0");
        textMap.put(Keys.NUMPAD_1, "1");
        textMap.put(Keys.NUMPAD_2, "2");
        textMap.put(Keys.NUMPAD_3, "3");
        textMap.put(Keys.NUMPAD_4, "4");
        textMap.put(Keys.NUMPAD_5, "5");
        textMap.put(Keys.NUMPAD_6, "6");
        textMap.put(Keys.NUMPAD_7, "7");
        textMap.put(Keys.NUMPAD_8, "8");
        textMap.put(Keys.NUMPAD_9, "9");
        textMap.put(Keys.NUMPAD_0, "0");
    }

    private boolean backspaceDown;
    private float backspaceDownTime;

    public void keyDown(int keycode) {
        inputYPosition = 207.5f;
        if (textMap.containsKey(keycode)) {
            currentInput += textMap.get(keycode);
//            inputYPosition = 207.5f;
            return;
        }
        if (keycode == Keys.BACKSPACE && currentInput.length() > 0) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            backspaceDown = true;
            backspaceDownTime = 0;
//            inputYPosition = 207.5f;
        } else if (keycode == Keys.ENTER || keycode == Keys.NUMPAD_ENTER) {
            inputFinished = true;
            finalText = currentInput;
        }
    }

    public void keyUp(int keycode) {
        backspaceDown = false;
    }

    public boolean isInputFinished() {
        return inputFinished;
    }

    public String getInputText() {
        return currentInput;
    }

    private final String titleText = "INPUT STARTING FLOOR";

    public void draw(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();
        lifetime += delta;

        float yOffset = (float)Math.sin(lifetime)*10;

        layout.setText(titleFont, titleText);
        titleFont.draw(batch, titleText, 400 - layout.width/2, 300 + yOffset);

        if (backspaceDown) {
            if (backspaceDownTime > 0.25 && currentInput.length() > 0) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                inputYPosition = 207.5f;
            } else {
                backspaceDownTime += delta;
            }
        }

        boolean empty = currentInput.equals("");
        BitmapFont currentInputFont = empty ? emptyInputFont : inputFont;
        String text = empty ? "1" : currentInput;

        layout.setText(currentInputFont, text);
        inputYPosition = Lerp.lerp(inputYPosition, 200, Lerp.alpha(delta, 10));
        currentInputFont.draw(batch, text, 400 - layout.width/2, inputYPosition + yOffset);
    }

    public void dispose() {
        titleFont.dispose();
        inputFont.dispose();
        emptyInputFont.dispose();
    }
}
