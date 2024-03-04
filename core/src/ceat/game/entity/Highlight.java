package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.Lerp;
import com.badlogic.gdx.Gdx;

public class Highlight extends BoardEntity {
    public Highlight(Game newGame, Grid newGrid) {
        super(newGame, newGrid);
        parentTile = grid.getTileAt(gridX, gridY);

        super.loadSprite("img/baseTile.png");
        sprite.setColor(1f, 1f, 1f, 0.5f);
        sprite.setScale(2f);
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
    }

    public void setGrid(Grid newGrid) {
        grid = newGrid;
    }

    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        float alpha = Lerp.alpha(delta, 20);
        x = Lerp.lerp(x, parentTile.x, alpha);
        y = Lerp.lerp(y, parentTile.y, alpha);
        sprite.setPosition(x, y);
    }
}
