package ceat.game;

import com.badlogic.gdx.math.Vector2;

public class Lerp {
    public static float lerp(float v1, float v2, float a) {
        return v1 + (v2 - v1)*a;
    }

    public static float alpha(float deltaTime, float speed) {
        return (float)(1.0 - Math.pow(1/speed, speed*deltaTime));
    }

    public static Vector2 threePointBezier(float x1, float y1, float x2, float y2, float x3, float y3, float a) {
        float newX1 = lerp(x1, x1, a);
        float newY1 = lerp(y1, y2, a);
        float newX2 = lerp(x2, x3, a);
        float newY2 = lerp(y2, y3, a);
        return new Vector2(lerp(newX1, newX2, a), lerp(newY1, newY2, a));
    }

    public static Vector2 threePointBezier(Vector2 p1, Vector2 p2, Vector2 p3, float a) {
       return threePointBezier(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, a);
    }
}
