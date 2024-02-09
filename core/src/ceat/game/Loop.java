package ceat.game;

import java.util.ArrayList;
public class Loop {
    private static ArrayList<Loop> loops = new ArrayList();
    private float elapsed;
    private float duration;
    public static void runLoops(float deltaTime) {
        int l = loops.size();
        for (int i = 0; i < l; i++) {
            Loop f = loops.get(i);
            f.elapsed += deltaTime;
            if (f.elapsed > f.duration) {
                loops.remove(f);
                i--;
            }
            else f.run(deltaTime, f.elapsed);
        }

    }
    public Loop(float dur) {
        duration = dur;
        loops.add(this);
    }
    public void run(float deltaTime, float elapsed) {}
}