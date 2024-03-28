package ceat.game.entity.enemy;

import ceat.game.*;
import ceat.game.entity.BoardEntity;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class AuraEnemy extends Enemy {
    private int turnState;
    private final TexSprite ringSprite;
    private float lifetime;
    public AuraEnemy(Game newGame, Grid newGrid) {
        super(newGame, newGrid, EnemyType.AURA);
        getSprite().setColor(1, 0, 1, 1);
        ringSprite = new TexSprite("img/tileOutline.png");
        ringSprite.setScale(6);
        ringSprite.setCenter();
        ringSprite.setColor(1, 0, 1, 0.25f);
    }

    public void step() {
        turnState++;
        if (turnState%2 != 0) return;
        super.step();
    }
    public TexSprite getRingSprite() {
        return ringSprite;
    }

    public void animateEntry() {
        animateEntry(1, 0, 1);
    }
    public void render() {
        lifetime += GameHandler.getDeltaTime();
        super.render();
        float cycle = (float)Math.sin(lifetime*5)/2 + 0.5f;
        ringSprite.setColor(1, 0, 1, Lerp.lerp(0.25f, 0.75f, cycle));
        Vector2 hi = getScreenPosition();
        Vector2 a = new Vector2(hi.x - 2 + (float)Math.random()*4, hi.y - 2 + (float)Math.random()*4);
        ringSprite.setPosition(ScreenOffset.project(a));
    }
    public void draw(SpriteBatch batch) {
        super.draw(batch);
//        ringSprite.draw(batch);
    }

//    public void step() {
//        turnState++;
//        if (turnState%2 != 0) return;
//        turnState = 0;
//        super.step();
//    }
    public void dispose() {
        ringSprite.dispose();
        super.dispose();
    }

    public boolean overlaps(BoardEntity other) {
        if (other.getType() == BoardEntityType.PLAYER) {
            int xDiff = Math.abs(getGridPosition().getX() - other.getGridPosition().getX());
            int yDiff = Math.abs(getGridPosition().getY() - other.getGridPosition().getY());
            return xDiff <= 1 && yDiff <= 1;
        }
        return super.overlaps(other);
    }

    public String toString() {
        return "MAGENTA ENEMY";
    }
    public boolean equals(AuraEnemy other) {
        return this == other;
    }
}
