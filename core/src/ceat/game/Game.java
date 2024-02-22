package ceat.game;

import ceat.game.entity.*;
import ceat.game.fx.Effect;
import ceat.game.fx.NewFloorBanner;
import ceat.game.fx.SkyBeam;
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
    private float gameTime;
    private Grid grid;
    private Grid nextGrid;
    private Grid lastGrid;
    private boolean lastGridPresent;
    private Music music;
    private boolean allowStep;
    public ArrayList<Effect> effects;
    public Player player;
    private int floor = 1;

    public Game(SpriteBatch newBatch) {
        batch = newBatch;

        effects = new ArrayList<>();

        grid = new Grid(this);
        nextGrid = new Grid(this);

        player = new Player(this, grid);
        grid.setPlayer(player);
        player.setGridPosition(Grid.width/2, Grid.height/2);

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

        ScreenUtils.clear(0.15f, 0.15f, 0.15f, 1);
        batch.begin();

        if (lastGridPresent)
            lastGrid.draw(batch);
        nextGrid.draw(batch);
        grid.draw(batch);
        for (Effect effect: effects) {
            effect.render();
            effect.draw(batch);
        }

        batch.end();
    }

    private void changeGrids() {
        Grid oldGrid = grid;

        lastGrid = grid;
        grid = nextGrid;
        nextGrid = new Grid(this);

        lastGridPresent = true;

        grid.setPlayer(player);

        Game hi = this;

        player.animateJump(grid.getTileAt(Grid.width/2, Grid.height/2), 1.15f, 400);
        oldGrid.explode();
        player.setGrid(grid);
        new ChainedTask().wait(0.2f).run(new Timer.Task() {
            @Override
            public void run() {
                player.setGridPosition(Grid.width/2, Grid.height/2);
            }
        }).wait(0.4f).run(new Timer.Task() {
            @Override
            public void run() {
                grid.setGridPosition(Grid.gridPosition.CENTER);
                nextGrid.setGridPosition(Grid.gridPosition.TOP);
                floor++;
                new NewFloorBanner(hi, floor).play();
            }
        });
        new ChainedTask().wait(1f).run(new Timer.Task() {
            @Override
            public void run() {
                lastGridPresent = false;
                lastGrid = null;
            }
        });
    }

    private attackMode currentAttackMode = attackMode.NONE;

    private void killEnemy(Enemy enemy) {
        grid.enemies.remove(enemy);
        enemy.animateDeath();
        enemy.dispose();
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

    private void beamAttack(ChainedTask chain) {
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
        if (grid.projectiles.isEmpty()) return;
        chain.run(new Timer.Task() {
            @Override
            public void run() {
                for (Projectile projectile: grid.projectiles) {
                    projectile.step();
                }
            }
        }).wait(0.1f).run(new Timer.Task() {
            @Override
            public void run() {
                processProjectilesAndEnemies();
            }
        });
    }

    private void playerStep(ChainedTask chain) {
        chain.run(new Timer.Task() {
            @Override
            public void run() {
                player.step();
            }
        }).wait(0.25f).run(new Timer.Task() {
            @Override
            public void run() {
                processPlayerAndEnemies();
            }
        });
    }

    private void enemyStep(ChainedTask chain) {
        chain.run(new Timer.Task() {
            @Override
            public void run() {
                for (Enemy enemy: grid.enemies)
                    enemy.step();
            }
        }).wait(0.25f).run(new Timer.Task() {
            @Override
            public void run() {
                processProjectilesAndEnemies();
                processPlayerAndEnemies();
            }
        });
    }

    private void spawnEnemies(ChainedTask chain) {
        chain.run(new Timer.Task() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i ++)
                    grid.addEnemy();
            }
        }).wait(0.2f).run(new Timer.Task() {
            @Override
            public void run() {
                processProjectilesAndEnemies();
            }
        });
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
                beamAttack(chain);
                break;
            }
            case CLEAR: {
                clearAttack();
            }
        }
        currentAttackMode = attackMode.NONE;

        projectileStep(chain);
        playerStep(chain);
        enemyStep(chain);
        spawnEnemies(chain);
        chain.run(new Timer.Task() {
            @Override
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
                changeGrids();
            }
        }
    }

    public void dispose() {

    }
}
