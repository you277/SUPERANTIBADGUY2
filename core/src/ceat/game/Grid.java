package ceat.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;

import ceat.game.entity.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Grid {
    public enum gridPosition {
        OFF,
        TOP,
        CENTER
    }
    public static int width = 10;
    public static int height = 10;
    public static Grid currentGrid;
    public static Game game;

    private static final float defaultY = 750;
    private static final float topCenterY = 500;
    private static final float mainCenterY = 250;

    public static Vector2 getFinalPosition(int proposedX, int proposedY) {
        int x;
        int y;
        if (proposedX < 0) x = width + proposedX;
        else if (proposedX >= width) x = proposedX - width;
        else x = proposedX;
        if (proposedY < 0) y = height + proposedY;
        else if (proposedY >= height) y = proposedY - height;
        else y = proposedY;
        return new Vector2(x, y);
    }

    public EmptyTile[][] grid;
    public ArrayList<Enemy> enemies;
    public ArrayList<Projectile> projectiles;
    public ArrayList<FreeProjectile> freeProjectiles;
    public Player player;

    public float centerX;
    public float centerY;
    public int totalEnemies;
    public int waveSpawnAmount;
    public int enemiesDead;
    private int floor;

    private gridPosition position;
    private final float wavePhaseShift;
    private boolean playerSet;
    private boolean isAnimating;


    public Grid(Game newGame, int floor) {
        grid = new EmptyTile[10][10];
        for (EmptyTile[] row: grid) {
            for (int i = 0; i < 10; i++) {
                row[i] = new EmptyTile(game, this);
            }
        }
        currentGrid = this;
        game = newGame;

        wavePhaseShift = (float)Math.random()*3.14159f;
        this.floor = floor;

        centerX = 400;
        centerY = defaultY;

        position = gridPosition.OFF;

        enemies = new ArrayList<>();
        projectiles = new ArrayList<>();
        freeProjectiles = new ArrayList<>();

        totalEnemies = (int)(Math.sqrt(floor)*7 + 0.5);
        waveSpawnAmount = Math.max((int)(Math.sqrt(floor) + 0.5), 1);
    }

    public void setPlayer(Player newPlayer) {
        player = newPlayer;
        playerSet = true;
    }

    public EmptyTile getTileAt(int x, int y) {
        return grid[x][y];
    }

    public boolean getIsFreeSpaceAvailable() {
        return 1 + enemies.size() < width*height; // default 1 for the player
    }

    public Vector2 getFreeSpace() {
        int x = (int)(Math.random()*width);
        int y = (int)(Math.random()*height);
        if (x == player.gridX && y == player.gridY)
            return getFreeSpace();
        for (Enemy enemy: enemies)
            if (enemy.gridX == x && enemy.gridY == y)
                return getFreeSpace();
        return new Vector2(x, y);
    }

    private Enemy randomEnemyType() {
        if (floor > 3) {
            if (floor > 10) {
//                if (Math.random() < 0.2) return new SentryEnemy(game, this);
                return new SentryEnemy(game, this);
            }
//            if (Math.random() < 0.2) return new FastEnemy(game, this);
            return new FastEnemy(game, this);
        }
        return new Enemy(game, this);
    }

    private void addEnemy() {
        Vector2 newPos = getFreeSpace();
        Enemy enemy = randomEnemyType();
        enemy.setGridPosition((int)newPos.x, (int)newPos.y);
        enemy.animateEntry();
        enemies.add(enemy);
    }

    public void addEnemies() {
        System.out.println(totalEnemies);
        System.out.println(waveSpawnAmount);
        if (!getIsFreeSpaceAvailable()) return;
        for (int i = 0; i < waveSpawnAmount; i++) {
            addEnemy();
            if (!getIsFreeSpaceAvailable()) return;
        }
    }

    public boolean didWin() {
        return enemiesDead >= totalEnemies;
    }

    public void addProjectile() {
        Projectile projectile = new Projectile(game, this, player, player.getDirection());
        projectile.animateSpawn();
        projectiles.add(projectile);
    }

    private static final float[] left = {-25, 12.5f};
    private static final float[] up = {25, 12.5f};
    private static final float[] right = {25, -12.5f};
    private static final float[] down = {-25, -12.5f};

    public Vector2 getSpritePositionFromGridPosition(int x, int y) {
        EmptyTile tile = grid[x][y];
        Vector2 vec = new Vector2();
        vec.set(tile.x, tile.y);
        return vec;
    }

    public void setGridPosition(gridPosition newPosition) {
        position = newPosition;
        if (newPosition == gridPosition.CENTER) {
            for (EmptyTile[] tiles : grid) {
                for (EmptyTile tile : tiles) {
                    tile.fadeIn();
                }
            }
        }
    }

    private void positionTiles(float centerX2, float centerY2, float gameTime) {
        double topLeftY = centerY2 + 100 + Math.sin(gameTime + wavePhaseShift)*20;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                EmptyTile tile = grid[x][y];
                double p = gameTime*2 + x*0.5 + y*0.5 + wavePhaseShift;
                tile.x = (float)(centerX2 + right[0]*x + down[0]*y + Math.cos(p)*5);
                tile.y = (float)(topLeftY + down[1]*y + right[1]*x + Math.sin(p)*5);
            }
        }
    }

    public void explode() {
        HashMap<EmptyTile, float[]> velocities = new HashMap<>();
        for (EmptyTile[] tiles: grid) {
            for (EmptyTile tile: tiles) {
                float xVelocity = -400 + (float)Math.random()*800;
                float yVelocity = 150 + (float)Math.random()*100;
                velocities.put(tile, new float[] {xVelocity, yVelocity});
            }
        }
        isAnimating = true;
        new Loop(1f) {
            public void run(float delta, float elapsed) {
                for (EmptyTile tile: velocities.keySet()) {
                    float[] velocity = velocities.get(tile);
                    tile.x += velocity[0]*delta;
                    tile.y += velocity[1]*delta;
                    velocity[1] -= 1000*delta;
                }
            }
        };
    }

    public void render(float gameTime) {
        float delta = Gdx.graphics.getDeltaTime();
        switch (position) {
            case CENTER:
                centerY = Lerp.lerp(centerY, mainCenterY, Lerp.alpha(delta, 5));
                break;
            case TOP:
                centerY = Lerp.lerp(centerY, topCenterY, Lerp.alpha(delta, 5));
        }
        if (!isAnimating)
            positionTiles(centerX, centerY, gameTime);
    }

    public void draw(SpriteBatch batch) {
        for (EmptyTile[] row: grid) {
            for (EmptyTile tile: row) {
                tile.draw(batch);
            }
        }
        for(Projectile proj: projectiles) {
            proj.render();
            proj.draw(batch);
        }
        for(Enemy enemy: enemies) {
            enemy.render();
            enemy.draw(batch);
        }
        if (playerSet) {
            player.render();
            player.draw(batch);
        }
        ArrayList<FreeProjectile> freeProjectilesToRemove = new ArrayList<>();
        for(FreeProjectile proj: freeProjectiles) {
            proj.render();
            if (proj.active) proj.draw(batch);
            else freeProjectilesToRemove.add(proj);
        }
        for (FreeProjectile proj: freeProjectilesToRemove) freeProjectiles.remove(proj);
    }

    public void dispose() {
        for (Projectile proj: projectiles)
            proj.dispose();
        for (FreeProjectile proj: freeProjectiles)
            proj.dispose();
        for (Enemy enemy: enemies)
            enemy.dispose();
        for (EmptyTile[] row: grid)
            for (EmptyTile tile: row)
                tile.dispose();
    }
}
