package ceat.game;

public class Enemy extends Entity {
    public final entityType type = entityType.ENEMY;

    public Enemy() {
        super.loadSprite("ing/what.png");
    }

    @Override
    public void step() {

    }
}
