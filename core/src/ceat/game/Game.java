package ceat.game;

import ceat.game.entity.*;
import ceat.game.entity.enemy.Enemy;
import ceat.game.entity.enemy.FastEnemy;
import ceat.game.entity.enemy.FreeEnemy;
import ceat.game.entity.enemy.SentryEnemy;
import ceat.game.fx.*;
import ceat.game.gameGui.CooldownBarList;
import ceat.game.gameGui.GameGui;
import ceat.game.gameGui.StatusText;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    private String musicName;
    private float gameTime;
    
    private Grid grid;
    private Grid nextGrid;
    private Grid lastGrid;
    private boolean lastGridPresent;
    private final Player player;
    
    private boolean allowStep;
    private int turns = 0;
    private int enemiesKilled = 0;
    private int enemiesIgnored = 0;
    private int floorsDone = 0;
    private int floor;
    private final int startFloor;
    private int shotsFired;
    private final boolean isBackgroundGame;
    private boolean useAltInput = false;
    private boolean inputActive = true;
    private final GameBackground background;

    private Sound beamAttackSound;
    private Sound playerDeadSound;
    private Sound playerDeadSound2;
    private Sound multiKillSound;

    public Game(SpriteBatch newBatch, int startingFloor, boolean isBackgroundGame) {
        GameHandler.speed = 1;
        batch = newBatch;

        startFloor = startingFloor;
        floor = startingFloor;

        grid = new Grid(this, startingFloor);
        nextGrid = new Grid(this, startingFloor + 1);

        player = new Player(this, grid);
        grid.setPlayer(player);
        player.setGridPosition(Grid.width/2, Grid.height/2);

        gameGui = new GameGui(this);

        this.isBackgroundGame = isBackgroundGame;
        gameGui.setEnabled(!isBackgroundGame);
        grid.setIsActive(true);

        // gamegui setup
        gameGui.getEnemyCounter().setAlive(grid.getTotalEnemies());
        gameGui.getEnemyCounter().setTotal(grid.getTotalEnemies());
//        gameGui.cardHolder.pullUp(0);

        grid.setGridPosition(Grid.gridPosition.CENTER);
        nextGrid.setGridPosition(Grid.gridPosition.TOP);

        allowStep = true;

        if (!isBackgroundGame) {
            music = Gdx.audio.newMusic(Gdx.files.internal("snd/FOCUS.mp3"));
            music.setLooping(true);
            music.play();
            musicName = "FOCUS";

            beamAttackSound = Gdx.audio.newSound(Gdx.files.internal("snd/Attack2_2.mp3"));
            playerDeadSound = Gdx.audio.newSound(Gdx.files.internal("snd/Kill10_2.mp3"));
            playerDeadSound2 = Gdx.audio.newSound(Gdx.files.internal("snd/Rebirth1.mp3"));
            multiKillSound = Gdx.audio.newSound(Gdx.files.internal("snd/Fight2_2.mp3"));
        }

        if (!isBackgroundGame)
            new NewFloorBanner(floor).play();

        new Transition.Out().play();
        background = new GameBackground();
        background.play();
    }

    public Player getPlayer() {
        return player;
    }
    public Grid getGrid() {
        return grid;
    }

    public void render() {
        float delta = GameHandler.getDeltaTime();
        gameTime += delta;
        Loop.runLoops();

        ScreenOffset.render(delta);
        processPlayerAndSentryProjectiles();

        if (lastGridPresent)
            lastGrid.render(gameTime);
        nextGrid.render(gameTime);
        grid.render(gameTime);

        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
        if (!isBackgroundGame) batch.begin();
        Effect.renderBackgroundEffects(batch);

        if (lastGridPresent)
            lastGrid.draw(batch);
        nextGrid.draw(batch);
        grid.draw(batch);

        gameGui.draw(batch);
        Effect.renderEffects(batch);
        if (!isBackgroundGame) batch.end();

        checkMusic();
    }

    private void checkMusic() {
        if (music == null) return;
        String targetMusicName = floor >= 20 ? "HEXAGONER" : "FOCUS";
        if (musicName.equals(targetMusicName)) return;
        musicName = targetMusicName;
        new ChainedTask().wait(1f).run(new Timer.Task() {
            public void run() {
                music.stop();
            }
        }).wait(0.5f).run(new Timer.Task() {
            @Override
            public void run() {
                music.dispose();
                music = Gdx.audio.newMusic(Gdx.files.internal("snd/" + targetMusicName + ".mp3"));
                music.setLooping(true);
                music.play();
            }
        });
    }

    private void changeGrids(ChainedTask chain) {
        Grid oldGrid = grid;
        floorsDone++;
        enemiesIgnored += oldGrid.getEnemies().size();

        lastGrid = grid;
        grid = nextGrid;
        nextGrid = new Grid(this, floor + 1);

        lastGrid.setIsActive(false);
        grid.setIsActive(true);

        gameGui.getEnemyCounter().setAlive(grid.getTotalEnemies());
        gameGui.getEnemyCounter().setTotal(grid.getTotalEnemies());

        lastGridPresent = true;

        grid.setPlayer(player);

        player.animateJump(grid.getTileAt(Grid.width/2, Grid.height/2), 1.15f, 400);
        oldGrid.explode();
        player.setGrid(grid);

        checkMusic();
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
        grid.setEnemiesDead(grid.getEnemiesDead() + 1);
        enemiesKilled++;
        grid.getEnemies().remove(enemy);
        enemy.dispose();
        grid.clearFreeProjectiles(enemy);
        gameGui.getEnemyCounter().setAlive(grid.getTotalEnemies() - grid.getEnemiesDead());
    }

    private int currentEnemyAmt;
    private void setCurrentEnemyAmt() {
        currentEnemyAmt = grid.getEnemies().size();
    }
    public void compareEnemyAmt() {
        if (isBackgroundGame) return;
        if (currentEnemyAmt - grid.getEnemies().size() >= 3)
            multiKillSound.play();
    }

    public void processProjectilesAndEnemies() {
        EntityQuery<Enemy, Projectile> query = new EntityQuery<Enemy, Projectile>().overlap(grid.getEnemies(), grid.getProjectiles());
        if (query.a.isEmpty()) return;
        ScreenOffset.shake(20f, 0.25f);
        for (Enemy enemy : query.a)
            killEnemy(enemy);
        for (Projectile projectile : query.b) {
            grid.getProjectiles().remove(projectile);
            projectile.dispose();
        }
    }

    private void onPlayerDeath(String killedBy) {
        if (!player.getIsAlive()) return;
        player.setIsAlive(false);
        ScreenOffset.shake(25f, 1f);
        player.kill();
        if (!isBackgroundGame)
            music.setVolume(0.3f);
        useAltInput = true;
        gameGui.getDeathScreen().set(startFloor, floor, floorsDone, enemiesKilled, enemiesIgnored, shotsFired, turns, killedBy).play();
        gameGui.setDeathScreenEnabled(true);
        new PlayerDeathEffect(player.getScreenPosition()).play();
        if (isBackgroundGame) return;
        playerDeadSound.play();
        playerDeadSound2.play();
        new Loop(Loop.loopType.UNSYNCED,2) {
            public void run(float delta, float elapsed) {
                GameHandler.speed = 1 - elapsed/2*0.9f;
            }
            public void onEnd() {
                GameHandler.speed = 0.1f;
            }
        };
    }

    public void processPlayerAndSentryProjectiles() {
        if (player.getIsJumping()) return;
        Vector2 playerPosition = player.getScreenPosition();
        // it was using the bottom left of the sprite even tho i had the center set to the sprite center
        float playerX = playerPosition.x + player.getSprite().getWidth()/2;
        float playerY = playerPosition.y + player.getSprite().getHeight()/2;
        int killDistance = 5; // in pixels
        for (FreeProjectile proj: grid.getFreeProjectiles()) {
            if (!proj.getAlive()) continue;
            float projX = proj.getPosition().x;
            float projY = proj.getPosition().y;
            if (Math.pow(projX - playerX, 2) + Math.pow(projY - playerY, 2) <= Math.pow(killDistance, 2)) {
                onPlayerDeath("GREEN ENEMY PROJECTILE");
                break;
            }
        }
    }

    public void processPlayerAndEnemies() {
        if (!player.getIsAlive()) return;
        for (Enemy enemy: grid.getEnemies()) {
            if (BoardEntity.overlap(player, enemy)) {
                onPlayerDeath(enemy.toString());
                return;
            }
        }
    }

    private void clearAttack() {
        shotsFired += 24;
        ScreenOffset.shake(5f, 0.25f);
        gameGui.getCooldownBarList().setBarProgress(2, 0);
        setCurrentEnemyAmt();
        for (int xOff = -2; xOff <= 2; xOff++) {
            for (int yOff = -2; yOff <= 2; yOff++) {
                if (xOff == 0 && yOff == 0) continue;
                IntVector2 finalPos = Grid.getFinalPosition(player.getGridPosition(), xOff, yOff);
                int gridX = finalPos.getX();
                int gridY = finalPos.getY();
                ArrayList<Enemy> enemiesToKill = new ArrayList<>();
                for (Enemy enemy: grid.getEnemies()) {
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
        if (!isBackgroundGame) {
            beamAttackSound.play(10);
        }
        compareEnemyAmt();
    }

    private enum beamDirection {
        HORIZONTAL,
        VERTICAL
    }

    private void theActualBeamAttack(beamDirection direction) {
        ArrayList<Enemy> enemiesToKill = new ArrayList<>();
        if (direction == beamDirection.VERTICAL) {
            int x = player.getGridPosition().getX();
            for (int i = 0; i < Grid.height; i++)
                for (Enemy enemy: grid.getEnemies())
                    if (BoardEntity.overlap(enemy, x, i))
                        enemiesToKill.add(enemy);
        } else {
            int y = player.getGridPosition().getY();
            for (int i = 0; i < Grid.width; i++)
                for (Enemy enemy: grid.getEnemies())
                    if (BoardEntity.overlap(enemy, i, y))
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
        setCurrentEnemyAmt();
        gameGui.getCooldownBarList().setBarProgress(1, 0);
        ScreenOffset.shake(5f, 0.25f);
        theActualBeamAttack(dir == Entity.moveDirection.UP || dir == Entity.moveDirection.DOWN ? beamDirection.VERTICAL : beamDirection.HORIZONTAL);
        new SkyBeam(grid.getTileAt(player.getGridPosition()))
            .setColor(1f, 1f, 1f)
            .setScale(10f, 100f)
            .setRotation(rotation)
            .play();
        if (!isBackgroundGame)
            beamAttackSound.play(10);
        compareEnemyAmt();
    }

    private void projectileStep(ChainedTask chain) {
        chain.run(new Timer.Task() {
            public void run() {
                if (grid.getProjectiles().isEmpty()) return;
                for (Projectile projectile: grid.getProjectiles()) {
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
                for (Enemy enemy: grid.getEnemies())
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
        grid.getEnemies().add(enemy);
    }

    public void spawnEnemies(ChainedTask chain) {
        chain.run(new Timer.Task() {
            public void run() {
                if (!grid.getIsFreeSpaceAvailable()) return;
                for (int i = 0; i < grid.getWaveSpawnAmount(); i++) {
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
    public void spawnEnemies() {
        spawnEnemies(new ChainedTask());
    }

    public void postEnemyProcesses() {
        if (grid.didWin()) changeGrids(new ChainedTask());
    }

    public void postEnemyProcesses(ChainedTask chain) {
        chain.run(new Timer.Task() {
            public void run() {
                if (grid.didWin()) changeGrids(chain);
            }
        });
    }

    private void endOfTurn(ChainedTask chain) {
        chain.run(new Timer.Task() {
            public void run() {
                gameGui.getCooldownBarList().setBarProgress(0, 1f);
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

        CooldownBarList cooldownBarList = gameGui.getCooldownBarList();
        StatusText statusText = gameGui.getStatusText();

        switch (currentAttackMode) {
            case BULLET: {
                shotsFired++;
                cooldownBarList.setBarProgress(0, 0f);
                grid.addProjectile();
                break;
            }
            case BEAM: {
                if (beamCooldown != 0) {
                    statusText.shake();
                    return;
                }
                didBeamAttack = true;
                beamAttack();
                beamCooldown = 2;
                break;
            }
            case CLEAR: {
                if (clearCooldown != 0) {
                    statusText.shake();
                    return;
                }
                didClearAttack = true;
                clearAttack();
                clearCooldown = 3;
            }
        }
        statusText.setText();
        currentAttackMode = attackMode.NONE;

        allowStep = false;
        turns++;

        if (beamCooldown != 0 && !didBeamAttack) {
            beamCooldown--;
            cooldownBarList.setBarProgress(1, 1 - beamCooldown/2f);
        }
        if (clearCooldown != 0 && !didClearAttack) {
            clearCooldown--;
            cooldownBarList.setBarProgress(2, 1 - clearCooldown/3f);
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
        StatusText statusText = gameGui.getStatusText();
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
                statusText.setText();
                break;
            }
            case Keys.NUMPAD_1:
            case Keys.NUM_1: {
                currentAttackMode = attackMode.BULLET;
                statusText.setText("USING ATTACK 1");
                statusText.pop();
                break;
            }
            case Keys.NUMPAD_2:
            case Keys.NUM_2: {
                currentAttackMode = attackMode.BEAM;
                statusText.setText(getTurnText(beamCooldown == 0, "USING ATTACK 2", "ATTACK 2 ON COOLDOWN"));
                statusText.pop();
                break;
            }
            case Keys.NUMPAD_3:
            case Keys.NUM_3: {
                currentAttackMode = attackMode.CLEAR;
                statusText.setText(getTurnText(clearCooldown == 0, "USING ATTACK 3", "ATTACK 3 ON COOLDOWN"));
                statusText.pop();
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
        if (!isBackgroundGame) {
            music.dispose();
            playerDeadSound.dispose();
            playerDeadSound2.dispose();
            beamAttackSound.dispose();
            multiKillSound.dispose();
        }
        background.stop();
        background.dispose();
        Loop.cancelAllLoops();
    }

    public String toString() {
        return "GAME";
    }
    public boolean equals(Game other) {
        return this == other;
    }
}
