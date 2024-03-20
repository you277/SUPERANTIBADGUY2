package ceat.game;

import java.util.ArrayList;
import java.util.HashMap;

import ceat.game.entity.*;
import ceat.game.entity.enemy.Enemy;
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

    public static IntVector2 getFinalPosition(int proposedX, int proposedY) {
        int x;
        int y;
        if (proposedX < 0) x = width + proposedX;
        else if (proposedX >= width) x = proposedX - width;
        else x = proposedX;
        if (proposedY < 0) y = height + proposedY;
        else if (proposedY >= height) y = proposedY - height;
        else y = proposedY;
        return new IntVector2(x, y);
    }

    public static IntVector2 getFinalPosition(IntVector2 initPosition, int xOffset, int yOffset) {
        return getFinalPosition(initPosition.getX() + xOffset, initPosition.getY() + yOffset);
    }


    private EmptyTile[][] grid;
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> projectiles;
    private ArrayList<FreeProjectile> freeProjectiles;
    private Player player;
    private boolean active;

    private float centerX;
    private float centerY;
    private int totalEnemies;
    private int waveSpawnAmount;
    private int enemiesDead;
    private final int floor;

    private gridPosition position;
    private final float wavePhaseShift;
    private boolean playerSet;
    private boolean isAnimating;
    private boolean shouldDraw;


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

        shouldDraw = true;
    }
    public void setIsActive(boolean active) {
        this.active = active;
    }
    public boolean getIsActive() {
        return active;
    }

    public void setPlayer(Player newPlayer) {
        player = newPlayer;
        playerSet = true;
    }
    public Player getPlayer() {
        return player;
    }

    public int getTotalEnemies() {
        return totalEnemies;
    }
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
    public ArrayList<FreeProjectile> getFreeProjectiles() {
        return freeProjectiles;
    }
    public int getWaveSpawnAmount() {
        return waveSpawnAmount;
    }

    public void setEnemiesDead(int enemiesDead) {
        this.enemiesDead = enemiesDead;
    }
    public int getEnemiesDead() {
        return  getEnemiesDead();
    }

    public EmptyTile getTileAt(int x, int y) {
        return grid[x][y];
    }
    public EmptyTile getTileAt(IntVector2 position) {
        return grid[position.getX()][position.getY()];
    }

    public boolean getIsFreeSpaceAvailable() {
        return 1 + enemies.size() < width*height; // default 1 for the player
    }

    public Vector2 getFreeSpace() {
        int x = (int)(Math.random()*width);
        int y = (int)(Math.random()*height);
        Vector2 position = new Vector2(x, y);
        if (player.getGridPosition().equals(position))
            return getFreeSpace();
        for (Enemy enemy: enemies)
            if (enemy.getGridPosition().equals(position))
                return getFreeSpace();
        return position;
    }

    public boolean didWin() {
        return enemiesDead >= totalEnemies;
    }

    public void addProjectile() {
        Projectile projectile = new Projectile(game, this, player, player.getDirection());
        projectiles.add(projectile);
    }

    private static final float[] left = {-25, 12.5f};
    private static final float[] up = {25, 12.5f};
    private static final float[] right = {25, -12.5f};
    private static final float[] down = {-25, -12.5f};

    public Vector2 getSpritePositionFromGridPosition(int x, int y) {
        return grid[x][y].getScreenPosition();
    }
    public Vector2 getSpritePositionFromGridPosition(float x, float y) {
        return getSpritePositionFromGridPosition((int)x, (int)y);
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
                tile.getScreenPosition().set(
                        (float)(centerX2 + right[0]*x + down[0]*y + Math.cos(p)*5),
                    (float)(topLeftY + down[1]*y + right[1]*x + Math.sin(p)*5)
                );
            }
        }
    }

    public void explode() {
        HashMap<EmptyTile, float[]> velocities = new HashMap<>();
        for (FreeProjectile proj: freeProjectiles)
            proj.kill();
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
                    Vector2 screenPosition = tile.getScreenPosition();
                    screenPosition.set(screenPosition.x + velocity[0]*delta, screenPosition.y + velocity[1]*delta);
                    velocity[1] -= 1000*delta;
                }
            }
            public void onEnd() {
                shouldDraw = false;
            }
        };
    }

    public void clearFreeProjectiles(Enemy enemy) {
        for (FreeProjectile proj: freeProjectiles)
            if (proj.parent == enemy)
                proj.kill();
    }

    public void render(float gameTime) {
        float delta = GameHandler.getDeltaTime();
        switch (position) {
            case CENTER:
                centerY = Lerp.lerp(centerY, mainCenterY, Lerp.alpha(delta, 5));
                break;
            case TOP:
                centerY = Lerp.lerp(centerY, topCenterY, Lerp.alpha(delta, 5));
        }
        if (!isAnimating)
            positionTiles(centerX, centerY, gameTime);

        for (Projectile proj: projectiles) proj.render();
        for (Enemy enemy: enemies) enemy.render();
        if (playerSet) player.render();
        ArrayList<FreeProjectile> freeProjectilesToRemove = new ArrayList<>();
        for(FreeProjectile proj: freeProjectiles) {
            proj.render();
            if (!proj.getActive()) freeProjectilesToRemove.add(proj);
        }
        for (FreeProjectile proj: freeProjectilesToRemove) freeProjectiles.remove(proj);
    }

    public void draw(SpriteBatch batch) {
        if (!shouldDraw) return;
        for (EmptyTile[] row: grid)
            for (EmptyTile tile: row)
                tile.draw(batch);

        for (Projectile proj: projectiles) proj.draw(batch);
        for (Enemy enemy: enemies) enemy.draw(batch);
        if (playerSet) player.draw(batch);

        for(FreeProjectile proj: freeProjectiles) proj.draw(batch);
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
