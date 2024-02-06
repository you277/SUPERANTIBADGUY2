package ceat.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import ceat.game.Lerp;

public class TheActualGame {
    private final SpriteBatch batch;
    private float elapsed;
    private Sprite guy;

    public TheActualGame(SpriteBatch newBatch) {
        batch = newBatch;

        Texture hi = new Texture("img/what.png");
        guy = new Sprite(hi);
        guy.setScale(0.5f, 0.5f);
    }

    private void stepGame(float delta, double elapsed) {
        float t = (float)(Math.min(elapsed, 2)/2);
        Vector2 pos1 = new Vector2();
        Vector2 pos2 = new Vector2();
        Vector2 pos3 = new Vector2();
        pos1.set(10, 10);
        pos2.set(400, 300);
        pos3.set(500, 10);

        Vector2 currentPos = Lerp.threePointBezier(pos1, pos2, pos3, t);
        guy.setPosition(currentPos.x, currentPos.y);
        guy.setRotation((float)elapsed*180);
    }
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        elapsed += delta;
        stepGame(delta, elapsed);

        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        guy.draw(batch);
        batch.end();
    }

    public void dispose() {

    }
}
