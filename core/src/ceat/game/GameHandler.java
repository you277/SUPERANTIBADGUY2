package ceat.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class GameHandler extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;

	Texture title1;
	Texture title2;

	private int screen = 0;
	private boolean gameRunning;
	private Game game;


	private void renderSplash1() {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		batch.draw(title1, 0, 0);
		batch.end();
	}

	private void renderSplash2() {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(title2, 0, 0);
		batch.end();
	}

	private void doGame() {
		Sound switchSound = Gdx.audio.newSound(Gdx.files.internal("snd/kb.mp3"));
		GameHandler hi = this;

		new ChainedTask()
				.run(new Task() {
					@Override
					public void run() {
						screen = 0;
						switchSound.play();
					}
				})
				.wait(1.5f)
			.run(new Task() {
				@Override
				public void run() {
					screen = 1;
					switchSound.play();
				}
			})
			.wait(1.5f)
			.run(new Task() {
				@Override
				public void run() {
					screen = 2;
					gameRunning = true;
					game = new Game(batch);
					switchSound.play();
					title1.dispose();
					title2.dispose();
					Gdx.input.setInputProcessor(hi);
				}
			})
			.wait(1.5f)
			.run(new Task() {
				@Override
				public void run() {
					switchSound.dispose();
				}
			});
	}
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		title1 = new Texture("img/title1.png");
		title2 = new Texture("img/title2.png");
		doGame();
	}

	@Override
	public void render() {
		if (screen == 0) renderSplash1();
		else if (screen == 1) renderSplash2();
		else {
			if (gameRunning) game.render();
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (gameRunning) game.keyDown(keycode);
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
	
	@Override
	public void dispose() {
		batch.dispose();
		title1.dispose();
	}
}
