package de.twiese.twhome.fx.support;

import java.util.Queue;
import java.util.concurrent.*;

public class ExecutorManager {

    private static Queue<ExecutorService> executorsToShutdown = new ConcurrentLinkedQueue<>();

    public static ScheduledExecutorService createSchedulerDaemon() {
        return Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public static ScheduledExecutorService createScheduler() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            return t;
        });
        executorsToShutdown.add(executor);
        return executor;
    }

    public static void shutDownAllExecutors() {
        executorsToShutdown.stream().forEach(ExecutorService::shutdown);
    }
}
