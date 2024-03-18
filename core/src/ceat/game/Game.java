package ceat.game;

import ceat.game.entity.*;
import ceat.game.entity.enemy.Enemy;
import ceat.game.entity.enemy.FastEnemy;
import ceat.game.entity.enemy.FreeEnemy;
import ceat.game.entity.enemy.SentryEnemy;
import ceat.game.fx.Effect;
import ceat.game.fx.NewFloorBanner;
import ceat.game.fx.SkyBeam;
import ceat.game.fx.Transition;
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
    private Music music;
    public float gameTime;
    
    private Grid grid;
    private Grid nextGrid;
    private Grid lastGrid;
    private boolean lastGridPresent;
    public Player player;
    
    private boolean allowStep;
    private int turns = 0;
    private int enemiesKilled = 0;
    private int enemiesIgnored = 0;
    private int floorsDone = 0;
    private int floor = 1;
    private int startFloor = 1;
    private int shotsFired = 0;
    public boolean isBackgroundGame;
    private boolean useAltInput = false;
    private boolean inputActive = true;

    public Game(SpriteBatch newBatch, int startingFloor, boolean isBackgroundGame) {
        GameHandler.speed = 1;
        batch = newBatch;

        startFloor = startingFloor;
        floor = startingFloor;

        gameGui = new GameGui(this);
        grid = new Grid(this, startingFloor);
        nextGrid = new Grid(this, startingFloor + 1);

        this.isBackgroundGame = isBackgroundGame;
        gameGui.enabled = !isBackgroundGame;
        grid.active = true;

        player = new Player(this, grid);
        grid.setPlayer(player);
        player.setGridPosition(Grid.width/2, Grid.height/2);

        // gamegui setup
        gameGui.enemyCounter.setAlive(grid.totalEnemies);
        gameGui.enemyCounter.setTotal(grid.totalEnemies);
//        gameGui.cardHolder.pullUp(0);

        grid.setGridPosition(Grid.gridPosition.CENTER);
        nextGrid.setGridPosition(Grid.gridPosition.TOP);

        allowStep = true;

        if (!isBackgroundGame) {
            music = Gdx.audio.newMusic(Gdx.files.internal("snd/Hexagonest.mp3"));
            music.setLooping(true);
            music.play();
        }

        if (!isBackgroundGame)
            new NewFloorBanner(floor).play();

        new Transition.Out().play();
    }

    public void render() {
        float delta = GameHandler.getDeltaTime();
        gameTime += delta;
        Loop.runLoops();

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

        gameGui.draw(batch);
        Effect.renderEffects(batch);
        batch.end();
    }

    private void changeGrids(ChainedTask chain) {
        Grid oldGrid = grid;
        floorsDone++;
        enemiesIgnored += oldGrid.enemies.size();

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
                if (!isBackgroundGame)
                    new NewFloorBanner(floor).play();
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
        enemiesKilled++;
        grid.enemies.remove(enemy);
        enemy.dispose();
        grid.clearFreeProjectiles(enemy);
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
        if (!player.isAlive) return;
        ScreenOffset.shake(25f, 1f);
        player.kill();
//        music.setVolume(0);
        useAltInput = true;
        gameGui.deathScreen.set(startFloor, floor, floorsDone, enemiesKilled, enemiesIgnored, shotsFired, turns).play();
        gameGui.deathScreenEnabled = true;
        if (isBackgroundGame) return;
        new Loop(Loop.loopType.UNSYNCED,2) {
            public void run(float delta, float elapsed) {
                GameHandler.speed = 1 - elapsed/2*0.9f;
            }
            public void onEnd() {
                GameHandler.speed = 0.1f;
            }
        };
    }

    public void processPlayerAndEnemies() {
        if (!player.isAlive) return;
        for (Enemy enemy: grid.enemies) {
            if (BoardEntity.overlap(player, enemy)) {
                onPlayerDeath();
                return;
            }
        }
    }

    private void clearAttack() {
        shotsFired += 24;
        ScreenOffset.shake(5f, 0.25f);
        gameGui.cooldownBarList.setBarProgress(2, 0);
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
                new SkyBeam(grid.getTileAt(gridX, gridY))
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
        shotsFired++;
        Entity.moveDirection dir = player.getDirection();
        float rotation =
            dir == Entity.moveDirection.UP ? -60 :
            dir == Entity.moveDirection.RIGHT ? -120 :
            dir == Entity.moveDirection.DOWN ? 120 :
            60 // left
            ;
        gameGui.cooldownBarList.setBarProgress(1, 0);
        ScreenOffset.shake(5f, 0.25f);
        theActualBeamAttack(dir == Entity.moveDirection.UP || dir == Entity.moveDirection.DOWN ? beamDirection.VERTICAL : beamDirection.HORIZONTAL);
        new SkyBeam(grid.getTileAt(player.gridX, player.gridY))
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
        if (floor >= 3) {
            if (floor >= 6) {
                if (floor >= 10) {
                    if (Math.random() < 0.3) return new SentryEnemy(this, grid);
                }
                if (Math.random() < 0.3) return new FreeEnemy(this, grid);
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

    private void postEnemyProcesses(ChainedTask chain) {
        chain.run(new Timer.Task() {
            public void run() {
                if (grid.didWin()) changeGrids(chain);
            }
        });
    }

    private void endOfTurn(ChainedTask chain) {
        chain.run(new Timer.Task() {
            public void run() {
                gameGui.cooldownBarList.setBarProgress(0, 1f);
            }
        });
    }

    private int beamCooldown = 0;
    private int clearCooldown = 0;

    private void doTurn() {
        if (!allowStep) return;

        ChainedTask chain = new ChainedTask();

        boolean didBeamAttack = false;
        boolean didClearAttack = false;

        switch (currentAttackMode) {
            case BULLET: {
                shotsFired++;
                gameGui.cooldownBarList.setBarProgress(0, 0f);
                grid.addProjectile();
                break;
            }
            case BEAM: {
                if (beamCooldown != 0) {
                    gameGui.turnText.shake();
                    return;
                }
                didBeamAttack = true;
                beamAttack();
                beamCooldown = 2;
                break;
            }
            case CLEAR: {
                if (clearCooldown != 0) {
                    gameGui.turnText.shake();
                    return;
                }
                didClearAttack = true;
                clearAttack();
                clearCooldown = 3;
            }
        }
        gameGui.turnText.text = "";
        currentAttackMode = attackMode.NONE;

        allowStep = false;
        turns++;

        if (beamCooldown != 0 && !didBeamAttack) {
            beamCooldown--;
            System.out.println(1 - beamCooldown/3);
            gameGui.cooldownBarList.setBarProgress(1, 1 - beamCooldown/2f);
        }
        if (clearCooldown != 0 && !didClearAttack) {
            clearCooldown--;
            gameGui.cooldownBarList.setBarProgress(2, 1 - clearCooldown/3f);
        }

        projectileStep(chain);
        playerStep(chain);
        enemyStep(chain);
        postEnemyProcesses(chain);
        spawnEnemies(chain);
        endOfTurn(chain);
        chain.run(new Timer.Task() {
            public void run() {
                allowStep = true;
            }
        });
    }

    private String getTurnText(boolean b, String s1, String s2) {
        return b ? s1 : s2;
    }

    private void altInput(int keycode) {
        switch (keycode) {
            case Keys.R:
                inputActive = false;
                new Transition.In() {
                    public void onFinish() {
                        restartGame(startFloor);
                    }
                }.play();
                break;
            case Keys.ESCAPE:
                inputActive = false;
                new Transition.In() {
                    public void onFinish() {
                        returnToTitle();
                    }
                }.play();
                break;
        }
    }

    // overridden by GameHandler
    public void restartGame(int floor) {}
    public void returnToTitle() {}

    public void keyDown(int keycode) {
        if (!inputActive) return;
        if (useAltInput) {
            altInput(keycode);
            return;
        }
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
//                gameGui.cardHolder.pullUp(0);
                gameGui.turnText.text = "";
                break;
            }
            case Keys.NUMPAD_1:
            case Keys.NUM_1: {
                currentAttackMode = attackMode.BULLET;
//                gameGui.cardHolder.pullUp(1);
                gameGui.turnText.text = "USING ATTACK 1";
                gameGui.turnText.pop();
                break;
            }
            case Keys.NUMPAD_2:
            case Keys.NUM_2: {
                currentAttackMode = attackMode.BEAM;
//                gameGui.cardHolder.pullUp(2);
                gameGui.turnText.text = getTurnText(beamCooldown == 0, "USING ATTACK 2", "ATTACK 2 ON COOLDOWN");
                gameGui.turnText.pop();
                break;
            }
            case Keys.NUMPAD_3:
            case Keys.NUM_3: {
                currentAttackMode = attackMode.CLEAR;
//                gameGui.cardHolder.pullUp(3);
                gameGui.turnText.text = getTurnText(clearCooldown == 0, "USING ATTACK 3", "ATTACK 3 ON COOLDOWN");
                gameGui.turnText.pop();
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
        if (music != null) music.dispose();
        Loop.cancelAllLoops();
    }
}
