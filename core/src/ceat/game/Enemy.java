package ceat.game;

public class Enemy extends Entity {
    public final entityType type = entityType.ENEMY;
    public int x;
    public int y;

    public Enemy(Grid newGrid) {
        super(newGrid);
        super.loadSprite("img/what.png");
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
    }

    @Override
    public void step() {

    }
}
