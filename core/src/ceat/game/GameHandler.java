package ceat.game;

import ceat.game.fx.NewFloorBanner;
import ceat.game.requirements.SuperRequirements;
import ceat.game.titleScreenGui.TitleScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer.Task;

public class GameHandler extends ApplicationAdapter implements InputProcessor {
	public static float speed = 1;
	public static float getRawDeltaTime() {
		return Gdx.graphics.getDeltaTime();
	}
	public static float getDeltaTime() {
		return getRawDeltaTime()*speed;
	}

	private SpriteBatch batch;

	private Texture title1;
	private Texture title2;

	private int screen = 0;
	private Game game;
	private TitleScreen titleScreen;

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
					NewFloorBanner.createFonts(); // mask the terribly slow font loading with the splash
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
//					game = new Game(batch, 1, true, true);
					switchSound.play();
					title1.dispose();
					title2.dispose();
					Gdx.input.setInputProcessor(hi);
					toTitleScreen();
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

	public void toTitleScreen() {
		titleScreen = new TitleScreen(batch) {
			public void startGame(int floor) {
				titleScreen.dispose();
				titleScreen = null;
				toGame(floor);
			}
		};
	}

	public void toGame(int startFloor) {
		game = new Game(batch, startFloor, false) {
			public void restartGame(int floor) {
				game.dispose();
				toGame(floor);
			}
			public void returnToTitle() {
				game.dispose();
				game = null;
				toTitleScreen();
			}
		};
	}
	
	@Override
	public void create() {
		new SuperRequirements().doThing();
		new SuperRequirements(5);
		batch = new SpriteBatch();
		title1 = new Texture("img/title1.png");
		title2 = new Texture("img/title2.png");
		Font.createGenerator();
		doGame();
	}

	@Override
	public void render() {
		if (screen == 0) renderSplash1();
		else if (screen == 1) renderSplash2();
		else {
			if (game != null || titleScreen != null) {
				batch.begin();
				if (game != null) game.render();
				if (titleScreen != null) titleScreen.render();
				batch.end();
			}
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (game != null) game.keyDown(keycode);
		if (titleScreen != null) titleScreen.keyDown(keycode);
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		if (titleScreen != null) titleScreen.keyUp(keycode);
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

	public String toString() {
		return "GAME HANDLER";
	}
	public boolean equals(GameHandler other) {
		return this == other;
	}
}
