package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import ceat.game.IntVector2;
import ceat.game.TexSprite;

public class Projectile extends BoardEntity {
    private final moveDirection direction;
    public int life = 5;
    public Projectile(Game newGame, Grid newGrid, Player player, moveDirection newDirection) {
        super(newGame, newGrid);
        TexSprite sprite = loadSprite("img/baseTile.png");
        sprite.setColor(1f, 1f, 1f, 1f);
        sprite.setScale(1.2f);
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);

        direction = newDirection;
        setGridPosition(player.getGridPosition());
    }

    public void step() {
        int x = getGridPosition().getX();
        int y = getGridPosition().getY();
        switch (direction) {
            case UP: {
                y -= 2;
                break;
            }
            case RIGHT: {
                x += 2;
                break;
            }
            case DOWN: {
                y += 2;
                break;
            }
            case LEFT: {
                x -= 2;
            }
        }
        IntVector2 newPosition = Grid.getFinalPosition(x, y);
        animateJump(getParentTile(), getGrid().getTileAt(newPosition), 0.1f, 0);
        setGridPosition(newPosition);
        life--;
    }

    public String toString() {
        return "PROJECTILE";
    }
    public boolean equals(Projectile other) {
        return getGridPosition().equals(other.getGridPosition()) && life == other.life;
    }
}
