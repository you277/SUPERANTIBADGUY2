package ceat.game.titleScreenGui;

import ceat.game.Font;
import ceat.game.Game;
import ceat.game.GameHandler;
import ceat.game.TexSprite;
import ceat.game.fx.BackgroundCircleThing;
import ceat.game.fx.Effect;
import ceat.game.fx.Transition;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class TitleScreen {
    private final Game backgroundGame;
    private final BackgroundGameAI backgroundAI;
    private final SpriteBatch batch;
    private float lifetime;
    private final BitmapFont font;
    private final BitmapFont controlsFont;

    private final TexSprite bgSprite;
    private FloorDialog floorDialog;
    private final Selection startSelect;
    private final Selection floorSelect;
    private final Selection controlsSelect;
    private final BackgroundCircleThing backgroundCircleThing;
    private int currentSelection;
    private boolean acceptInput;
    private final Music music;
    private final Sound clickSound;
    private final Sound keySound;
    public TitleScreen(SpriteBatch newBatch) {
        GameHandler.speed = 1;

        batch = newBatch;
        backgroundGame = new Game(batch, 10, true);
        backgroundAI = new BackgroundGameAI(backgroundGame);

        font = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 20;
            }
        });
        controlsFont = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 15;
            }
        });
        acceptInput = true;

        bgSprite = new TexSprite("img/square.png");

        bgSprite.setScale(800, 600);
        bgSprite.setPosition(400, 300);
        bgSprite.setColor(0, 0, 0, 0.4f);

        currentSelection = 0;
        startSelect = new Selection("START FLOOR 1").setPosition(0, 300).setSelected(true);
        floorSelect = new Selection("SELECT FLOOR").setPosition(0, 200).setSelected(false);
        controlsSelect = new Selection("CONTROLS").setPosition(0, 100).setSelected(false);

        backgroundCircleThing = new BackgroundCircleThing();
        backgroundCircleThing.play();

        clickSound = Gdx.audio.newSound(Gdx.files.internal("snd/Switch1.mp3"));
        keySound = Gdx.audio.newSound(Gdx.files.internal("snd/Key2.mp3"));

        music = Gdx.audio.newMusic(Gdx.files.internal("snd/ETERNALWAVES.mp3"));
        music.setLooping(true);
        music.play();
    }

    public void render() {
        float delta = GameHandler.getDeltaTime();
        lifetime += delta;

        backgroundAI.step();

        Effect.renderEffects();
        Effect.drawBackgroundEffects(batch);
        backgroundGame.render();

        bgSprite.draw(batch);
        float offset = (float)Math.cos(lifetime)*5;
        font.draw(batch, "SUPERBADGUYDESTROYER2200++ ULTRA DELUXE\nEDITION", 15, 470 + offset);
        startSelect.draw(batch);
        floorSelect.draw(batch);
        controlsSelect.draw(batch);
        if (floorDialog != null)
            floorDialog.draw(batch);
        Effect.drawEffects(batch);

        if (currentSelection == 2) {
            controlsFont.draw(batch, "[W][A][S][D] CHANGE DIRECTION\n[<][^][v][>]\n\n[1][2][3][4] SELECT ATTACK\n[0] DESELECT ATTACK\n\n[ENTER] DO TURN", 350, 250);
        }
    }

    public void keyDown(int keycode) {
        if (!acceptInput) return;
        if (floorDialog != null) {
            floorDialog.keyDown(keycode);
            return;
        }
        int previousSelection = currentSelection;
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
            case Keys.NUM_3:
            case Keys.NUMPAD_3:
                currentSelection = 2;
                break;
            case Keys.ENTER:
            case Keys.NUMPAD_ENTER:
                keySound.play();
                if (currentSelection == 0) {
                    transitionToGame(1);
                } else if (currentSelection == 1) {
                    floorDialog = new FloorDialog() {
                        public void onConfirm(int floor) {
                            keySound.play();
                            floorDialog.dispose();
                            floorDialog = null;
                            transitionToGame(floor);
                        }
                        public void onCancel() {
                            keySound.play();
                            floorDialog.dispose();
                            floorDialog = null;
                        }
                    };
                }
                break;
        }
        if (currentSelection < 0) currentSelection = 2;
        else if (currentSelection > 2) currentSelection = 0;
        if (currentSelection == 0) {
            startSelect.setSelected(true);
            floorSelect.setSelected(false);
            controlsSelect.setSelected(false);
        } else if (currentSelection == 1) {
            startSelect.setSelected(false);
            floorSelect.setSelected(true);
            controlsSelect.setSelected(false);
        } else {
            startSelect.setSelected(false);
            floorSelect.setSelected(false);
            controlsSelect.setSelected(true);
        }
        if (previousSelection != currentSelection) {
            clickSound.play();
        }
    }

    public void keyUp(int keycode) {
        if (!acceptInput) return;
        if (floorDialog != null) floorDialog.keyUp(keycode);
    }
    public void transitionToGame(int floor) {
        acceptInput = false;
        new Transition.In() {
            public void onFinish() {
                startGame(floor);
            }
        }.play();
    }
    public void startGame(int floor) {}
    public void dispose() {
        startSelect.dispose();
        floorSelect.dispose();
        controlsSelect.dispose();
        bgSprite.dispose();
        font.dispose();
        controlsFont.dispose();
        backgroundGame.dispose();
        clickSound.dispose();
        keySound.dispose();
        backgroundCircleThing.stop();
        backgroundCircleThing.unregisterEffect();
        music.dispose();
    }

    public String toString() {
        return "TITLE SCREEN";
    }
    public boolean equals(TitleScreen other) {
        return this == other;
    }
}
