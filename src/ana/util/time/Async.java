package ana.util.time;


// dep

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


// main

public class Async {
    public static Timer doLoop(long msRate, Runnable... events) {
        String uuid = UUID.randomUUID().toString();

        Timer loopTimer = new Timer(uuid, true);
        loopTimer.scheduleAtFixedRate(
            new TimerTask() {
                @Override
                public void run() {
                    for (Runnable e : events) {
                        e.run();
                    }
                }
            },
            0, msRate
        );

        return loopTimer;
    }
}
