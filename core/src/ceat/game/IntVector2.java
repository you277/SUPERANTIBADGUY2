package ceat.game;

import com.badlogic.gdx.math.Vector2;

public class IntVector2 {
    private int x;
    private int y;
    public IntVector2() {}
    public IntVector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(IntVector2 other) {
        x = other.x;
        y = other.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int[] getPositionArr() {
        return new int[] {x, y};
    }

    public boolean equals(IntVector2 other) {
        return x == other.x && y == other.y;
    }

    public boolean equals(Vector2 other) {
        return x == other.x && y == other.y;
    }
}
