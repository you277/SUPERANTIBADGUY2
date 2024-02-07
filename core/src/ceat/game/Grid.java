package ceat.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

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

    private EmptyTile[][] grid;
    private ArrayList<Enemy> enemies;
//    private ArrayList<Projectile> projectiles;
    private Player player;


    public Grid() {
        grid = new EmptyTile[10][10];
        for (EmptyTile[] row: grid) {
            for (int i = 0; i < 10; i++) {
                row[i] = new EmptyTile(this);
            }
        }
        currentGrid = this;

        enemies = new ArrayList();
        player = new Player(this);
    }

    public Entity getEntityAt(int x, int y) {
        return grid[x][y];
    }

    private static int[] left = {-25, 10};
    private static int[] up = {25, 10};
    private static int[] right = {25, -10};
    private static int[] down = {-25, -10};

    public Vector2 getSpritePositionFromGridPosition(int x, int y) {
        EmptyTile tile = grid[x][y];
        Vector2 vec = new Vector2();
        vec.set(tile.x, tile.y);
        return vec;
    }

    public void render(float gameTime) {
        double topLeftX = 400;
        double topLeftY = 400 + Math.sin(gameTime)*20;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                EmptyTile tile = grid[x][y];
                tile.x = (float)(topLeftX + right[0]*x + down[1]*y);
                tile.y = (float)(topLeftY + down[0]*y + right[1]*x);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (EmptyTile[] row: grid) {
            for (EmptyTile tile: row) {
                tile.draw(batch);
            }
        }
        for(Enemy enemy: enemies) {
            enemy.draw(batch);
        }
//        for(Projectile proj: projectiles) {
//            proj.draw(batch);
//        }
//        player.draw(batch);
    }

//    public Vector2 getTilePosition(int x, int y) {
//
//    }
}
