package ceat.game;

import ceat.game.fx.Effect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

public class Game implements InputProcessor {
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
        Gdx.input.setInputProcessor(this);

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

    private void doTurn() {
        new ChainedTask()
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
                    for (Enemy enemy: grid.enemies) {
                        enemy.step();
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
            });
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W: {
                player.setDirection(Entity.moveDirection.UP);
                break;
            }
            case Keys.A: {
                player.setDirection(Entity.moveDirection.LEFT);
                break;
            }
            case Keys.S: {
                player.setDirection(Entity.moveDirection.DOWN);
                break;
            }
            case Keys.D: {
                player.setDirection(Entity.moveDirection.RIGHT);
                break;
            }
            case Keys.ENTER: {
                if (allowStep) {
                    allowStep = false;
                    doTurn();
                    new ChainedTask()
                        .wait(0.25f)
                        .run(new Timer.Task() {
                            @Override
                            public void run() {
                                allowStep = true;
                            }
                        });
                }
                break;
            }
            case Keys.L: {
                changeGrids();
            }
        }
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public void dispose() {

    }
}
