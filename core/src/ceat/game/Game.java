package ceat.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	Texture title1;

	private void renderTitle1() {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(title1, 10, 10);
		batch.end();
	}

	private void renderTitle2() {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		title1 = new Texture("what.png");
	}

	@Override
	public void render() {
//		ScreenUtils.clear(1, 0, 0, 1);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
		renderTitle1();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		title1.dispose();
		img.dispose();
	}
}
