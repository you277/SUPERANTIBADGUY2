package ceat.game;

import java.util.ArrayList;

import ceat.game.fx.BeamEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Grid {
    public static int width = 10;
    public static int height = 10;
    public static Grid currentGrid;
    public static TheActualGame game;

    public static Vector2 getFinalPosition(int proposedX, int proposedY) {
        int x;
        int y;
        if (proposedX < 0) x = width + proposedX;
        else if (proposedX >= width) x = proposedX - width;
        else x = proposedX;
        if (proposedY < 0) y = height + proposedY;
        else if (proposedY >= height) y = proposedY - height;
        else y = proposedY;
        Vector2 newVec = new Vector2();
        newVec.set(x, y);
        return newVec;
    }

    public EmptyTile[][] grid;
    public ArrayList<Enemy> enemies;
//    private ArrayList<Projectile> projectiles;
    public Player player;


    public Grid(TheActualGame newGame) {
        grid = new EmptyTile[10][10];
        for (EmptyTile[] row: grid) {
            for (int i = 0; i < 10; i++) {
                row[i] = new EmptyTile(game, this);
            }
        }
        currentGrid = this;
        game = newGame;

        enemies = new ArrayList();
        player = new Player(game, this);
        player.setGridPosition(width/2, height/2);
    }

    public EmptyTile getTileAt(int x, int y) {
        return grid[x][y];
    }

    public void addEnemy() {
        Enemy enemy = new Enemy(game, this);
        enemy.setGridPosition((int)(Math.random()*width), (int)(Math.random()*height));
        enemy.animateEntry();
//        new BeamEffect(game, enemy.parentTile)
//                .setColor(1f, 0f, 0f)
//                .setScale(5f, 100f).play();
        enemies.add(enemy);
    }

    private static int[] left = {-20, 20};
    private static int[] up = {20, 20};
    private static int[] right = {20, -20};
    private static int[] down = {-20, -20};

    public Vector2 getSpritePositionFromGridPosition(int x, int y) {
        EmptyTile tile = grid[x][y];
        Vector2 vec = new Vector2();
        vec.set(tile.x, tile.y);
        return vec;
    }

    public void render(float gameTime) {
        double topLeftX = 400;
        double topLeftY = 425 + Math.sin(gameTime)*20;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                EmptyTile tile = grid[x][y];
                tile.x = (float)(topLeftX + right[0]*x + down[1]*y + Math.cos(gameTime*2 + x*0.5 + y*0.5)*5);
                tile.y = (float)(topLeftY + down[0]*y + right[1]*x + Math.sin(gameTime*2 + x*0.5 + y*0.5)*5);
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
            enemy.render();
            enemy.draw(batch);
        }
//        for(Projectile proj: projectiles) {
//            proj.draw(batch);
//        }
        player.draw(batch);
    }

//    public Vector2 getTilePosition(int x, int y) {
//
//    }
}
