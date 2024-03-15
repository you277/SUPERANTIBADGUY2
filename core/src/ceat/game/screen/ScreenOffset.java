package ceat.game.screen;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class ScreenOffset {
    private static final ArrayList<ScreenShake> shakers = new ArrayList<>();
    public static float offsetX = 0;
    public static float offsetY = 0;
    public static Vector2 project(float x, float y) {
        return new Vector2(x + offsetX, y + offsetY);
    }

    public static void shake(float strength, float duration) {
        shakers.add(new ScreenShake(duration, strength));
    }

    private static void renderShakes(float delta) {
        for (int i = 0; i < shakers.size(); i++) {
            ScreenShake shake = shakers.get(i);
            offsetY += (-1 + (float)Math.random()*2)*shake.strength * (1 - shake.existenceTime/shake.duration);
            shake.existenceTime += delta;
            if (shake.existenceTime > shake.duration) {
                shakers.remove(i);
                i--;
            }
        }
    }

    public static void render(float delta) {
        offsetX = 0;
        offsetY = 0;
        renderShakes(delta);
    }
}
