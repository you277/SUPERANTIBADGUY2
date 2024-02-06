package ceat.game;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class ChainedTask {
    private float time = 0;
    public ChainedTask wait(float t) {
        time += t;
        return this;
    }

    public ChainedTask run(Task task) {
        Timer.schedule(task, time);
        return this;
    }
}
