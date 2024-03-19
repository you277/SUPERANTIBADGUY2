package ceat.game.gameGui;

import ceat.game.ChainedTask;
import ceat.game.Font;
import ceat.game.Loop;
import ceat.game.TexSprite;
import ceat.game.fx.Effect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Timer;

public class DeathScreen extends Effect {
    private final TexSprite bgSprite;
    private final GlyphLayout layout;

    private final BitmapFont titleFont;
    private final BitmapFont killedByFont;
    private final BitmapFont statFont;
    private Sound tickSound;
    private int startFloor;
    private String killedBy;

    private class IntState {
        public int num;
        public int displayNum;
        public IntState(int num) {
            this.num = num;
            displayNum = 0;
        }
        public void play() {
            float duration = Math.max(Math.min((float)num/1000, 7.5f), 1);
            new Loop(Loop.loopType.UNSYNCED, duration) {
                public void run(float delta, float elapsed) {
                    int previous = displayNum;
                    displayNum = (int)(elapsed/duration * num);
                    if (previous != displayNum) {
                        tickSound.play(10);
                    }
                }
                public void onEnd() {
                    displayNum = num;
                    if (num != 0) new ChainedTask().wait(0.5f).run(new Timer.Task() {
                        public void run() {
                            onFinish();
                        }
                    });
                    else onFinish();
                }
            };
        }
        public void onFinish() {}
    }

    private class Stat {
        private String name;
        private int value;
        private IntState state;
        private float statsTop;
        private boolean visible;
        private Stat statToPlayWhenFinished;
        public Stat(String name, int value, float statsTop) {
            this.name = name;
            this.value = value;
            this.statsTop = statsTop;
            state = new IntState(value) {
                public void onFinish() {
                    if (statToPlayWhenFinished != null) statToPlayWhenFinished.play();
                }
            };
        }

        public void play() {
            visible = true;
            state.play();
        }

        public void playWhenFinished(Stat otherStat) {
            statToPlayWhenFinished = otherStat;
        }

        public void draw(SpriteBatch batch) {
            if (!visible) return;
            drawStat(batch, name, "" + state.displayNum, statsTop);
        }
    }

    private Stat[] statLines;

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
        killedByFont = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 10;
            }
        });
        statFont = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 15;
            }
        });

        zIndex = 2;
    }

    public DeathScreen set(int startFloor, int finalFloor, int floorsDone, int enemiesKilled, int enemiesIgnored, int shotsFired, int turnsTaken, String killedBy) {
        this.startFloor = startFloor;
        this.killedBy = killedBy;

        float statsTop = 300;
        float increment = 25;

        statLines = new Stat[] {
                new Stat("FINAL FLOOR", finalFloor, statsTop),
                new Stat("FLOORS CONQUERED", floorsDone, statsTop - increment),
                new Stat("ENEMIES KILLED", enemiesKilled, statsTop - increment*2),
                new Stat("ENEMIES IGNORED", enemiesIgnored, statsTop - increment*3),
                new Stat("SHOTS FIRED", shotsFired, statsTop - increment*4),
                new Stat("TURNS TAKEN", turnsTaken, statsTop - increment*5),
        };
        for (int i = 1; i < statLines.length; i++)
            statLines[i - 1].playWhenFinished(statLines[i]);
        return this;
    }

    public void drawStat(SpriteBatch batch, String name, String value, float height) {
        layout.setText(statFont, name);
        statFont.draw(batch, name, 400 - layout.width - 10, height);
        layout.setText(statFont,  value);
        statFont.draw(batch, value, 400 + 10, height);
    }

    public void play() {
        registerEffect();
        tickSound = Gdx.audio.newSound(Gdx.files.internal("snd/click.mp3"));
        statLines[0].play();
    }

    public void draw(SpriteBatch batch) {
        bgSprite.draw(batch);

        layout.setText(titleFont, "YOU ARE DEAD");
        titleFont.draw(batch, "YOU ARE DEAD", 400 - layout.width/2, 400);
        if (killedBy != null) {
            String str = "KILLED BY " + killedBy;
            layout.setText(killedByFont, str);
            killedByFont.draw(batch, str, 400 - layout.width/2, 360);
        }

        for (Stat stat: statLines)
            stat.draw(batch);

        String hi = "[ESC] TO TITLE - [R] TO RETURN TO FLOOR [" + startFloor + "]";
        layout.setText(statFont, hi);
        statFont.draw(batch, hi, 400 - layout.width/2, 75);
    }

    public void dispose() {
        unregisterEffect();
        titleFont.dispose();
        killedByFont.dispose();
        statFont.dispose();
        tickSound.dispose();
    }
}
