package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.Lerp;
import com.badlogic.gdx.Gdx;

public class Highlight extends BoardEntity {
    public Highlight(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        setParentTile(getGrid().getTileAt(getGridPosition()));

        super.loadSprite("img/baseTile.png");
        sprite.setColor(1f, 1f, 1f, 0.5f);
        sprite.setScale(2f);
        sprite.setCenter();
    }

    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        float alpha = Lerp.alpha(delta, 20);
        float x = Lerp.lerp(getScreenPosition().x, getParentTile().getScreenPosition().x, alpha);
        float y = Lerp.lerp(getScreenPosition().y, getParentTile().getScreenPosition().y, alpha);
        getScreenPosition().set(x, y);
        sprite.setPosition(x, y);
    }
}
