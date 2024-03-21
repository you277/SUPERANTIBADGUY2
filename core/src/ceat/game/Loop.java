package ceat.game;

import java.util.ArrayList;
public class Loop {
    public enum loopType {
        SYNCED,
        UNSYNCED
    }
    private final static ArrayList<Loop> loops = new ArrayList<>();

    private float elapsed;
    private final float duration;
    private final loopType type;
    public static void runLoops() {
        float syncedDelta = GameHandler.getDeltaTime();
        float unsyncedDelta = GameHandler.getRawDeltaTime();
        for (int i = 0; i < loops.size(); i++) {
            Loop f = loops.get(i);
            float deltaTime = f.type == loopType.SYNCED ? syncedDelta : unsyncedDelta;
            f.elapsed += deltaTime;
            if (f.elapsed > f.duration) {
                loops.remove(f);
                f.onEnd();
                i--;
            }
            else f.run(deltaTime, f.elapsed);
        }

    }
    public static void cancelAllLoops() {
        while (!loops.isEmpty()) {
            Loop f = loops.get(0);
            loops.remove(f);
            f.onEnd();
        }
    }
    public Loop(loopType t, float dur) {
        duration = dur;
        type = t;
        loops.add(this);
    }
    public Loop(float dur) {
        this(loopType.SYNCED, dur);
    }
    public void run(float deltaTime, float elapsed) {}
    public void onEnd() {}

    public String toString() {
        if (type == loopType.SYNCED) return "SYNCED LOOP";
        return "UNSYNCED LOOP";
    }
    public boolean equals(Loop other) {
        return this == other;
    }
}