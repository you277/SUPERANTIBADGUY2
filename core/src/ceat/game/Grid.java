package ceat.game;

import com.badlogic.gdx.math.Vector2;

import ceat.game.Entity;

public class Grid {
    public static int width = 10;
    public static int height = 10;
    public static Grid currentGrid;

    public static Vector2 getFinalPosition(int proposedX, int proposedY) {
        int x;
        int y;
        if (proposedX < 0) x = width + proposedX + 1;
        else if (proposedX >= width) x = proposedX - width + 1;
        else x = proposedX;
        if (proposedY < 0) y = height + proposedY + 1;
        else if (proposedY >= height) y = proposedY - height + 1;
        else y = proposedY;
        Vector2 newVec = new Vector2();
        newVec.set(x, y);
        return newVec;
    }

    private Entity[][] grid;

    public Grid() {
        grid = new Entity[10][10];
        for (Entity[] row: grid) {
            for (int i = 0; i < 10; i++) {
                row[i] = new Entity();
            }
        }
        currentGrid = this;
    }

    public Entity getEntityAt(int x, int y) {
        return grid[x][y];
    }

    public void replaceEntityAt(int x, int y, Entity newEntity) {
        grid[x][y].dispose();
        grid[x][y] = newEntity;
    }
}
