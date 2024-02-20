package ceat.game;

import ceat.game.entity.*;
import ceat.game.fx.Effect;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
        oldGrid.explode();

        lastGrid = grid;
        grid = nextGrid;
        nextGrid = new Grid(this);

        lastGridPresent = true;

        grid.setPlayer(player);

        player.animateJump(grid.getTileAt(Grid.width/2, Grid.height/2), 0.5f, 400);
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

    private void processProjectilesAndEnemies(EntityQuery<Enemy, Projectile> query) {
        if (query.a.isEmpty()) return;
        ScreenOffset.shake(20f, 0.25f);
        for (Enemy enemy : query.a) {
            grid.enemies.remove(enemy);
            enemy.animateDeath();
            enemy.dispose();
        }
        for (Projectile projectile : query.b) {
            grid.projectiles.remove(projectile);
            projectile.dispose();
        }
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
        }
        currentAttackMode = attackMode.NONE;

        if (!grid.projectiles.isEmpty()) {
            for (Projectile projectile: grid.projectiles) {
                projectile.step();
            }
            chain.wait(0.1f)
                .run(new Timer.Task() {
                    @Override
                    public void run() {
                        processProjectilesAndEnemies(new EntityQuery<Enemy, Projectile>().overlap(grid.enemies, grid.projectiles));
                    }
                });
        }
        chain
            .run(new Timer.Task() {
                @Override
                public void run() {
                    player.step();
                }
            })
            .wait(0.25f)
            .run(new Timer.Task() {
                @Override
                public void run() {
                    for (Enemy enemy: grid.enemies)
                        enemy.step();
                    EntityQuery<Enemy, Projectile> queryAgain = new EntityQuery<Enemy, Projectile>().overlap(grid.enemies, grid.projectiles);
                    if (!queryAgain.a.isEmpty()) {
                        new ChainedTask().wait(0.25f).run(new Timer.Task() {
                            @Override
                            public void run() {
                                processProjectilesAndEnemies(queryAgain);
                            }
                        });
                    }
                }
            })
            .wait(0.25f)
            .run(new Timer.Task() {
                @Override
                public void run() {
                    for (int i = 0; i < 3; i ++)
                        grid.addEnemy();
                }
            }).wait(0.2f).run(new Timer.Task() {
                    @Override
                    public void run() {
                        processProjectilesAndEnemies(new EntityQuery<Enemy, Projectile>().overlap(grid.enemies, grid.projectiles));
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
