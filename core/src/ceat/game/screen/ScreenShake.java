package ceat.game.screen;

public class ScreenShake {
    public float duration;
    public float existenceTime;
    public float strength;

    public ScreenShake(float duration, float strength) {
        this.duration = duration;
        this.strength = strength;
        existenceTime = 0;
    }

    public String toString() {
        return "SCREEN SHAKE";
    }
    public boolean equals(ScreenShake other) {
        return duration == other.duration && strength == other.strength;
    }
}
