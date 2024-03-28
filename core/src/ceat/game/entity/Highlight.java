package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.Lerp;
import ceat.game.TexSprite;
import com.badlogic.gdx.Gdx;

public class Highlight extends BoardEntity {
    public Highlight(Game newGame, Grid newGrid) {
        super(newGame, newGrid, BoardEntityType.HIGHLIGHT);
        setParentTile(getGrid().getTileAt(getGridPosition()));

        TexSprite sprite = loadSprite("img/baseTile.png");
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
        getSprite().setPosition(x, y);
    }

    public String toString() {
        return "HIGHLIGHT";
    }
    public boolean equals(Highlight other) {
        return this == other;
    }
}
