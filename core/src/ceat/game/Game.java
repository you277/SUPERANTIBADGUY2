package ceat.game;

import ceat.game.entity.*;
import ceat.game.fx.Effect;
import ceat.game.fx.NewFloorBanner;
import ceat.game.fx.SkyBeam;
import ceat.game.gameGui.GameGui;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

public class Game {
    public enum attackMode {
        NONE,
        BULLET,
        BEAM,
        CLEAR
    }

    private final SpriteBatch batch;
    private final GameGui gameGui;
    private final Music music;
    public float gameTime;
    
    private Grid grid;
    private Grid nextGrid;
    private Grid lastGrid;
    private boolean lastGridPresent;
    
    public ArrayList<Effect> effects;
    public Player player;
    
    private boolean allowStep;
    private int turns = 0;
    private int floor = 1;

    public Game(SpriteBatch newBatch) {
        batch = newBatch;
        effects = new ArrayList<>();

        gameGui = new GameGui(this);
        grid = new Grid(this, 1);
        nextGrid = new Grid(this, 2);

        grid.active = true;

        player = new Player(this, grid);
        grid.setPlayer(player);
        player.setGridPosition(Grid.width/2, Grid.height/2);

        gameGui.enemyCounter.setAlive(grid.totalEnemies);
        gameGui.enemyCounter.setTotal(grid.totalEnemies);

        grid.setGridPosition(Grid.gridPosition.CENTER);
        nextGrid.setGridPosition(Grid.gridPosition.TOP);

        allowStep = true;

        music = Gdx.audio.newMusic(Gdx.files.internal("snd/Hexagonest.mp3"));
        music.setLooping(true);
        music.play();

        new NewFloorBanner(this, 1).play();
    }

    private void stepGame(float delta, double elapsed) {
        Loop.runLoops(delta);
    }
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        gameTime += delta;
        stepGame(delta, gameTime);

        ScreenOffset.render(delta);

        if (lastGridPresent)
            lastGrid.render(gameTime);
        nextGrid.render(gameTime);
        grid.render(gameTime);

        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
        batch.begin();

        if (lastGridPresent)
            lastGrid.draw(batch);
        nextGrid.draw(batch);
        grid.draw(batch);
        for (Effect effect: effects) {
            effect.render();
            effect.draw(batch);
        }

        gameGui.draw(batch);
        batch.end();
    }

    private void changeGrids(ChainedTask chain) {
        Grid oldGrid = grid;

        lastGrid = grid;
        grid = nextGrid;
        nextGrid = new Grid(this, floor + 1);

        lastGrid.active = false;
        grid.active = true;

        gameGui.enemyCounter.setAlive(grid.totalEnemies);
        gameGui.enemyCounter.setTotal(grid.totalEnemies);

        lastGridPresent = true;

        grid.setPlayer(player);

        Game hi = this;

        player.animateJump(grid.getTileAt(Grid.width/2, Grid.height/2), 1.15f, 400);
        oldGrid.explode();
        player.setGrid(grid);
        new ChainedTask().wait(0.2f).run(new Timer.Task() {
            public void run() {
                player.setGridPosition(Grid.width/2, Grid.height/2);
            }
        }).wait(0.4f).run(new Timer.Task() {
            public void run() {
                grid.setGridPosition(Grid.gridPosition.CENTER);
                nextGrid.setGridPosition(Grid.gridPosition.TOP);
                floor++;
                new NewFloorBanner(hi, floor).play();
            }
        });
        chain.wait(1f).run(new Timer.Task() {
            public void run() {
                lastGridPresent = false;
                lastGrid = null;
            }
        });
    }

    private attackMode currentAttackMode = attackMode.NONE;

    private void killEnemy(Enemy enemy) {
        grid.enemiesDead++;
        grid.enemies.remove(enemy);
        enemy.dispose();
        gameGui.enemyCounter.setAlive(grid.totalEnemies - grid.enemiesDead);
    }

    private void processProjectilesAndEnemies() {
        EntityQuery<Enemy, Projectile> query = new EntityQuery<Enemy, Projectile>().overlap(grid.enemies, grid.projectiles);
        if (query.a.isEmpty()) return;
        ScreenOffset.shake(20f, 0.25f);
        for (Enemy enemy : query.a)
            killEnemy(enemy);
        for (Projectile projectile : query.b) {
            grid.projectiles.remove(projectile);
            projectile.dispose();
        }
    }

    private void onPlayerDeath() {
//        player.kill();
//        music.setVolume(0);
    }

    private void processPlayerAndEnemies() {
        if (!player.isAlive) return;
        for (Enemy enemy: grid.enemies) {
            if (BoardEntity.overlap(player, enemy)) {
                onPlayerDeath();
                return;
            }
        }
    }

    private void clearAttack() {
        ScreenOffset.shake(5f, 0.25f);
        for (int xOff = -2; xOff <= 2; xOff++) {
            for (int yOff = -2; yOff <= 2; yOff++) {
                if (xOff == 0 && yOff == 0) continue;
                Vector2 finalPos = Grid.getFinalPosition(player.gridX + xOff, player.gridY + yOff);
                int gridX = (int)finalPos.x;
                int gridY = (int)finalPos.y;
                ArrayList<Enemy> enemiesToKill = new ArrayList<>();
                for (Enemy enemy: grid.enemies) {
                    if (BoardEntity.overlap(enemy, gridX, gridY)) {
                        enemiesToKill.add(enemy);
                    }
                }
                if (!enemiesToKill.isEmpty()) {
                    ScreenOffset.shake(17f, 0.25f);
                    for (Enemy enemy: enemiesToKill)
                        killEnemy(enemy);
                }
                new SkyBeam(this, grid.getTileAt(gridX, gridY))
                        .setColor(1f, 1f, 1f)
                        .setScale(10f, 10f).play();
            }
        }
    }

    private enum beamDirection {
        HORIZONTAL,
        VERTICAL
    }

    private void theActualBeamAttack(beamDirection direction) {
        ArrayList<Enemy> enemiesToKill = new ArrayList<>();
        if (direction == beamDirection.VERTICAL) {
            for (int i = 0; i < Grid.height; i++)
                for (Enemy enemy: grid.enemies)
                    if (BoardEntity.overlap(enemy, player.gridX, i))
                        enemiesToKill.add(enemy);
        } else {
            for (int i = 0; i < Grid.width; i++)
                for (Enemy enemy: grid.enemies)
                    if (BoardEntity.overlap(enemy, i, player.gridY))
                        enemiesToKill.add(enemy);
        }
        if (!enemiesToKill.isEmpty()) {
            ScreenOffset.shake(35f, 0.5f);
            for (Enemy enemy: enemiesToKill)
                killEnemy(enemy);
        }
    }

    private void beamAttack() {
        Entity.moveDirection dir = player.getDirection();
        float rotation =
            dir == Entity.moveDirection.UP ? -60 :
            dir == Entity.moveDirection.RIGHT ? -120 :
            dir == Entity.moveDirection.DOWN ? 120 :
            60 // left
            ;
        ScreenOffset.shake(5f, 0.25f);
        theActualBeamAttack(dir == Entity.moveDirection.UP || dir == Entity.moveDirection.DOWN ? beamDirection.VERTICAL : beamDirection.HORIZONTAL);
        new SkyBeam(this, grid.getTileAt(player.gridX, player.gridY))
            .setColor(1f, 1f, 1f)
            .setScale(10f, 100f)
            .setRotation(rotation)
            .play();
    }

    private void projectileStep(ChainedTask chain) {
        chain.run(new Timer.Task() {
            public void run() {
                if (grid.projectiles.isEmpty()) return;
                for (Projectile projectile: grid.projectiles) {
                    projectile.step();
                }
            }
        }).wait(0.1f).run(new Timer.Task() {
            public void run() {
                processProjectilesAndEnemies();
            }
        });
    }

    private void playerStep(ChainedTask chain) {
        chain.run(new Timer.Task() {
            public void run() {
                player.step();
            }
        }).wait(0.25f).run(new Timer.Task() {
            public void run() {
                processPlayerAndEnemies();
            }
        });
    }

    private void enemyStep(ChainedTask chain) {
        chain.run(new Timer.Task() {
            public void run() {
                for (Enemy enemy: grid.enemies)
                    enemy.step();
            }
        }).wait(0.25f).run(new Timer.Task() {
            public void run() {
                processProjectilesAndEnemies();
                processPlayerAndEnemies();
            }
        });
    }

    private Enemy randomEnemyType() {
        if (floor > 3) {
            if (floor > 10) {
                if (Math.random() < 0.3) return new SentryEnemy(this, grid);
            }
            if (Math.random() < 0.3) return new FastEnemy(this, grid);
        }
        return new Enemy(this, grid);
    }

    private void addEnemy() {
        Vector2 newPos = grid.getFreeSpace();
        Enemy enemy = randomEnemyType();
        enemy.setGridPosition((int)newPos.x, (int)newPos.y);
        enemy.animateEntry();
        grid.enemies.add(enemy);
    }

    private void spawnEnemies(ChainedTask chain) {
        chain.run(new Timer.Task() {
            public void run() {
                if (!grid.getIsFreeSpaceAvailable()) return;
                for (int i = 0; i < grid.waveSpawnAmount; i++) {
                    addEnemy();
                    if (!grid.getIsFreeSpaceAvailable()) return;
                }
            }
        }).wait(0.2f).run(new Timer.Task() {
            public void run() {
                processProjectilesAndEnemies();
            }
        });
    }

    private void endOfTurn(ChainedTask chain) {
        if (grid.didWin()) changeGrids(chain);
    }

    private void doTurn() {
        if (!allowStep) return;
        allowStep = false;

        ChainedTask chain = new ChainedTask();

        switch (currentAttackMode) {
            case BULLET: {
                grid.addProjectile();
                break;
            }
            case BEAM: {
                beamAttack();
                break;
            }
            case CLEAR: {
                clearAttack();
            }
        }
        currentAttackMode = attackMode.NONE;

        turns++;

        projectileStep(chain);
        playerStep(chain);
        enemyStep(chain);
        spawnEnemies(chain);
        endOfTurn(chain);
        chain.run(new Timer.Task() {
            public void run() {
                allowStep = true;
            }
        });
    }

    public void keyDown(int keycode) {
        switch (keycode) {
            case Keys.UP:
            case Keys.W: {
                player.setDirection(Entity.moveDirection.UP);
                break;
            }
            case Keys.LEFT:
            case Keys.A: {
                player.setDirection(Entity.moveDirection.LEFT);
                break;
            }
            case Keys.DOWN:
            case Keys.S: {
                player.setDirection(Entity.moveDirection.DOWN);
                break;
            }
            case Keys.RIGHT:
            case Keys.D: {
                player.setDirection(Entity.moveDirection.RIGHT);
                break;
            }
            case Keys.NUMPAD_0:
            case Keys.NUM_0: {
                currentAttackMode = attackMode.NONE;
                break;
            }
            case Keys.NUMPAD_1:
            case Keys.NUM_1: {
                currentAttackMode = attackMode.BULLET;
                break;
            }
            case Keys.NUMPAD_2:
            case Keys.NUM_2: {
                currentAttackMode = attackMode.BEAM;
                break;
            }
            case Keys.NUMPAD_3:
            case Keys.NUM_3: {
                currentAttackMode = attackMode.CLEAR;
                break;
            }
            case Keys.NUMPAD_ENTER:
            case Keys.ENTER: {
                doTurn();
                break;
            }
            case Keys.L: {
                changeGrids(new ChainedTask());
            }
        }
    }

    public void dispose() {
        player.dispose();
        grid.dispose();
        nextGrid.dispose();
        if (lastGridPresent) lastGrid.dispose();
        gameGui.dispose();
    }
}
