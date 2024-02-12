package ceat.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;

public class TheActualGame implements InputProcessor {
    private final SpriteBatch batch;
    private float gameTime;
    private Sprite guy;
    private Grid grid;

    public TheActualGame(SpriteBatch newBatch) {
        batch = newBatch;

        Texture hi = new Texture("img/what.png");
        guy = new Sprite(hi);
        guy.setScale(0.5f, 0.5f);

        grid = new Grid(this);
        Gdx.input.setInputProcessor(this);
    }

    private void stepGame(float delta, double elapsed) {
        Loop.runLoops(delta);

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
        gameTime += delta;
        stepGame(delta, gameTime);

        grid.render(gameTime);

        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        guy.draw(batch);
        grid.draw(batch);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W: {
                grid.player.setDirection(Entity.moveDirection.UP);
                break;
            }
            case Keys.A: {
                grid.player.setDirection(Entity.moveDirection.LEFT);
                break;
            }
            case Keys.S: {
                grid.player.setDirection(Entity.moveDirection.DOWN);
                break;
            }
            case Keys.D: {
                grid.player.setDirection(Entity.moveDirection.RIGHT);
                break;
            }
            case Keys.ENTER: {
                grid.player.step();
            }
        }
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public void dispose() {

    }
}
