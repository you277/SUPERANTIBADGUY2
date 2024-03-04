package ceat.game.entity;

import ceat.game.Game;
import ceat.game.Grid;
import com.badlogic.gdx.math.Vector2;

public class Projectile extends BoardEntity {
    private final moveDirection direction;
    public int life = 5;
    public Projectile(Game newGame, Grid newGrid, Player player, moveDirection newDirection) {
        super(newGame, newGrid);
        super.loadSprite("img/baseTile.png");
        sprite.setColor(1f, 1f, 1f, 1f);
        sprite.setScale(1.2f);
        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);

        direction = newDirection;
        setGridPosition(player.gridX, player.gridY);
    }

    public void animateSpawn() {

    }

    public void step() {
        int x = gridX;
        int y = gridY;
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
        Vector2 newPosition = Grid.getFinalPosition(x, y);
        int newX = (int)newPosition.x;
        int newY = (int)newPosition.y;
        animateJump(grid.getTileAt(newX, newY), 0.1f, 0);
        gridX = newX;
        gridY = newY;
        life--;
    }
}
