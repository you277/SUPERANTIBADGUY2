package ceat.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {
    public final entityType type = entityType.PLAYER;
    private moveDirection direction;
    private final Texture leftArrowTex;
    private final Texture upArrowTex;
    private final Texture rightArrowTex;
    private final Texture downArrowTex;
    private final Sprite leftArrow;
    private final Sprite upArrow;
    private final Sprite rightArrow;
    private final Sprite downArrow;

    public int gridX;
    public int gridY;

    public Player(Grid newGrid) {
        super(newGrid);
        super.loadSprite("img/what.png");
        leftArrowTex = new Texture("img/leftArr.png");
        upArrowTex = new Texture("img/upArr.png");
        rightArrowTex = new Texture("img/rightArr.png");
        downArrowTex = new Texture("img/downArr.png");
        leftArrow = new Sprite(leftArrowTex);
        upArrow = new Sprite(upArrowTex);
        rightArrow = new Sprite(rightArrowTex);
        downArrow = new Sprite(downArrowTex);
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
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        switch(direction) {
            case LEFT:
                leftArrow.draw(batch);
                break;
            case UP:
                upArrow.draw(batch);
                break;
            case RIGHT:
                rightArrow.draw(batch);
                break;
            case DOWN:
                downArrow.draw(batch);
                break;
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
