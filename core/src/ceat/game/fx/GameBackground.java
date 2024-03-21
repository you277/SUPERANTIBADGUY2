package ceat.game.fx;

import ceat.game.GameHandler;
import ceat.game.TexSprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameBackground extends Effect {
    private final TexSprite[] sprites;
    private float lifetime;
    private float yLevel;
    public GameBackground() {
        int rows = 8;
        int tilesPerRow = 11;
        sprites = new TexSprite[rows*tilesPerRow];
        for (int i = 0; i < rows*tilesPerRow; i++) {
            TexSprite sprite = new TexSprite("img/tileOutline.png");
            sprite.setScale(4);
            sprite.setCenter();
            sprites[i] = sprite;
        }
        arrangeTiles();
        setZIndex(-5);
    }

    public void play() {
        registerEffect();
    }

    public void arrangeTiles() {
//        int numRows = sprites.length/11;
        float top = 175;
//        float bottom = yLevel*sprites[0].getHeight()*5;
        float xOffset = (float)Math.sin(lifetime/6)*sprites[0].getWidth()*2.5f - sprites[0].getWidth()*2.5f;
        float opacity = 0.5f;
        for (int i = 0; i < sprites.length; i++) {
            int row = i/11;
            int column = i%11;
            TexSprite sprite = sprites[i];
            float yPosition = top - 0.5f*sprite.getHeight()*5*row - yLevel*sprite.getHeight()*5;
            float yOffset = (float)Math.cos(yPosition*0.05)*3.5f;
            sprite.setPosition(
                    column*sprite.getWidth()*5 + row%2*0.5f*sprite.getWidth()*5 + xOffset,
                    yPosition + yOffset
            );
            sprite.setColor(0.2f, 0.2f, 0.2f, (1 - (yPosition/top))*opacity);
        }
    }

    public void render() {
        float delta = GameHandler.getDeltaTime();
        lifetime += delta;
        yLevel = (yLevel + delta*0.3f)%1;
        arrangeTiles();
    }

    public void draw(SpriteBatch batch) {
        for (TexSprite sprite: sprites)
            sprite.draw(batch);
    }

    public void stop() {
        unregisterEffect();
        dispose();
    }
    public void dispose() {
        for (TexSprite sprite: sprites)
            sprite.dispose();
    }

    public String toString() {
        return "GAME BACKGROUND";
    }
    public boolean equals(GameBackground other) {
        return this == other;
    }
}
