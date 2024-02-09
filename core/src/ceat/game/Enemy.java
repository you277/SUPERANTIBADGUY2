package ceat.game;

public class Enemy extends BoardEntity {
    public final entityType type = entityType.ENEMY;

    public Enemy(TheActualGame newGame, Grid newGrid) {
        super(newGame, newGrid);
        super.loadSprite("img/what.png");
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
    }

    @Override
    public void step() {

    }
}
