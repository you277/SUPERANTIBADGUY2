package ceat.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends BoardEntity {
    public final entityType type = entityType.PLAYER;
    private moveDirection direction = moveDirection.UP;
    private final Texture leftArrowTex;
    private final Texture upArrowTex;
    private final Texture rightArrowTex;
    private final Texture downArrowTex;
    private final Sprite leftArrow;
    private final Sprite upArrow;
    private final Sprite rightArrow;
    private final Sprite downArrow;


    public Player(TheActualGame newGame, Grid newGrid) {
        super(newGame, newGrid);
        super.loadSprite("img/playerTile.png");
        sprite.setScale(2f);

        leftArrowTex = new Texture("img/leftArr.png");
        upArrowTex = new Texture("img/upArr.png");
        rightArrowTex = new Texture("img/rightArr.png");
        downArrowTex = new Texture("img/downArr.png");

        leftArrow = new Sprite(leftArrowTex);
        upArrow = new Sprite(upArrowTex);
        rightArrow = new Sprite(rightArrowTex);
        downArrow = new Sprite(downArrowTex);

        leftArrow.scale(1.5f);
        upArrow.scale(1.5f);
        rightArrow.scale(1.5f);
        downArrow.scale(1.5f);

        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
        leftArrow.setCenter(leftArrow.getWidth()/2, leftArrow.getHeight()/2);
        upArrow.setCenter(upArrow.getWidth()/2, upArrow.getHeight()/2);
        rightArrow.setCenter(rightArrow.getWidth()/2, rightArrow.getHeight()/2);
        downArrow.setCenter(downArrow.getWidth()/2, downArrow.getHeight()/2);
    }

    public moveDirection getDirection() {
        return direction;
    }

    public void setDirection(moveDirection newDirection) {
        direction = newDirection;
    }

    @Override
    public void step() {
        Vector2 vec;
        switch(direction) {
            case LEFT:
                vec = Grid.getFinalPosition(gridX - 1, gridY);
                break;
            case UP:
                vec = Grid.getFinalPosition(gridX, gridY - 1);
                break;
            case RIGHT:
                vec = Grid.getFinalPosition(gridX + 1, gridY);
                break;
            case DOWN:
                vec = Grid.getFinalPosition(gridX, gridY + 1);
                break;
            default:
                vec = new Vector2();
                vec.set(gridX, gridY);
        }
        gridX = (int)vec.x;
        gridY = (int)vec.y;
        super.animateJump(grid.getTileAt(gridX, gridY));
    }

    @Override
    public void draw(SpriteBatch batch) {
        Vector2 spritePos = grid.getSpritePositionFromGridPosition(gridX, gridY);
        super.render();
        super.draw(batch);
//        sprite.setPosition(spritePos.x, spritePos.y);
//        super.draw(batch);

        // switch statement threw an exception so i have to use ifs
        if (direction == moveDirection.LEFT) {
            leftArrow.setPosition(spritePos.x, spritePos.y);
            leftArrow.draw(batch);
        } else if (direction == moveDirection.UP) {
            upArrow.setPosition(spritePos.x, spritePos.y);
            upArrow.draw(batch);
        } else if (direction == moveDirection.RIGHT) {
            rightArrow.setPosition(spritePos.x, spritePos.y);
            rightArrow.draw(batch);
        } else if (direction == moveDirection.DOWN) {
            downArrow.setPosition(spritePos.x, spritePos.y);
            downArrow.draw(batch);
        }
    }

    @Override
    public void dispose() {
        tex.dispose();
        leftArrowTex.dispose();
        upArrowTex.dispose();
        rightArrowTex.dispose();
        downArrowTex.dispose();
    }
}
