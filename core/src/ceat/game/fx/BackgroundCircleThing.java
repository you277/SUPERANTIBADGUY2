package ceat.game.fx;

import ceat.game.GameHandler;
import ceat.game.TexSprite;

public class BackgroundCircleThing extends Effect {
    private float lifetime;
    public BackgroundCircleThing() {
        TexSprite sprite = loadSprite("img/ringThingy.png");
        sprite.setScale(5);
        sprite.setPosition(400, 250);
        sprite.setCenter();
        sprite.setColor(1, 1, 1, 0.1f);
        setZIndex(-3);
    }
    public void play() {
        registerEffect();
    }
    public void render() {
        lifetime += GameHandler.getDeltaTime();
        TexSprite sprite = getSprite();
        sprite.setRotation(-lifetime*5);
        sprite.setPosition(400, 250);
        sprite.setCenter();
    }
    public void stop() {
        unregisterEffect();
    }

    public String toString() {
        return "BACKGROUND CIRCLE THING";
    }
    public boolean equals(BackgroundCircleThing other) {
        return this == other;
    }
}
