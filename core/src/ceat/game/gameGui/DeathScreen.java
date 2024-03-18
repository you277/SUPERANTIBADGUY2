package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.Loop;
import ceat.game.TexSprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class DeathScreen {
    private final TexSprite bgSprite;
    private final GlyphLayout layout;

    private final BitmapFont titleFont;
    private final BitmapFont statFont;
    private int startFloor;
    private class IntState {
        public int num;
        public int displayNum;
        public IntState(int num) {
            this.num = num;
            displayNum = 0;
        }
        public void play() {
            float duration = Math.max(Math.min((float)num/1000, 3), 0.5f);
            new Loop(Loop.loopType.UNSYNCED, duration) {
                public void run(float delta, float elapsed) {
                    displayNum = (int)(elapsed/duration * num);
                }
                public void onEnd() {
                    displayNum = num;
                    onFinish();
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
        statFont = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 15;
            }
        });
    }

    public DeathScreen set(int startFloor, int finalFloor, int floorsDone, int enemiesKilled, int enemiesIgnored, int shotsFired, int turnsTaken) {
        this.startFloor = startFloor;

        float statsTop = 300;
        float increment = 25;

//        drawStat(batch, "FINAL FLOOR", "" + finalFloor, statsTop);
//        drawStat(batch, "FLOORS CONQUERED", "" + floorsDone, statsTop - 25);
//        drawStat(batch, "ENEMIES KILLED", "" + enemiesKilled, statsTop - 50);
//        drawStat(batch, "ENEMIES IGNORED", "" + enemiesIgnored, statsTop - 75);
//        drawStat(batch, "SHOTS FIRED", "" + shotsFired, statsTop - 100);
//        drawStat(batch, "TURNS TAKEN", "" + turnsTaken, statsTop - 125);

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
        statLines[0].play();
    }

    public void draw(SpriteBatch batch) {
        bgSprite.draw(batch);

        layout.setText(titleFont, "YOU ARE DEAD");
        titleFont.draw(batch, "YOU ARE DEAD", 400 - layout.width/2, 400);

        for (Stat stat: statLines)
            stat.draw(batch);

        String hi = "[ESC] TO TITLE - [R] TO RETURN TO FLOOR [" + startFloor + "]";
        layout.setText(statFont, hi);
        statFont.draw(batch, hi, 400 - layout.width/2, 75);
    }
}
