package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.entity.enemy.Enemy;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

public class EnemyIntroLabel {
    private static final HashMap<Enemy.EnemyType, Boolean> introducedEnemies = new HashMap<>();
    public static boolean hasEnemyTypeBeenIntroduced(Enemy.EnemyType type) {
        return introducedEnemies.containsKey(type);
    }
    public static void setEnemyTypeIntroduced(Enemy.EnemyType type) {
        introducedEnemies.put(type, true);
    }
    public static String getEnemyIntroText(Enemy.EnemyType type) {
        switch (type) {
            case AURA:
                return "DANGEROUS AURA";
            case FREE:
                return "THIS ENEMY DOES\nNOT CARE ABOUT\nWHEN YOU PRESS\nENTER\nTHIS GUY\nWILL BE THE\nBANE OF YOUR\nEXISTENCE";
            case SENTRY:
                return "SHOOTS BULLETS";
            case FAST:
                return "MOVES TWO SPACES";
            default:
                return "THE ENEMY";
        }
    }
    private final BitmapFont font;
    private String text;
    private Enemy parentEnemy;
    private int lifetime;

    public EnemyIntroLabel() {
        font = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 7;
                params.borderWidth = 1;
                params.borderColor = new Color(0.1f, 0.1f, 0.1f, 1);
            }
        });
    }
    public EnemyIntroLabel setParentEnemy(Enemy enemy) {
        parentEnemy = enemy;
        return this;
    }

    public EnemyIntroLabel setText(String text) {
        this.text = text;
        return this;
    }

    public Enemy getParentEnemy() {
        return parentEnemy;
    }

    public void incrementLifetime() {
        lifetime++;
    }
    public int getLifetime() {
        return lifetime;
    }

    public void draw(SpriteBatch batch) {
        Vector2 parentPosition = parentEnemy.getScreenPosition();
        float sizeX = parentEnemy.getSprite().getWidth();
        Vector2 position = ScreenOffset.project(
                parentPosition.x + sizeX/2 - Font.getTextWidth(font, text)/2,
                parentPosition.y + Font.getTextHeight(font, text)+ 20
        );
        font.draw(batch, text, position.x, position.y);
    }

    public void dispose() {
        font.dispose();
    }

    public String toString() {
        return "ENEMY INTRO LABEL";
    }
    public boolean equals() {
        return false;
    }
}
