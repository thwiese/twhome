package de.twiese.twhome.fx.support;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Function;

public class ExecutorManager {

    private static Queue<ExecutorService> executorsToShutdown = new ConcurrentLinkedQueue<>();

    public static ScheduledExecutorService createSchedulerDaemon() {
        return Executors.newSingleThreadScheduledExecutor(daemanThreadFactory);
    }

    public static ScheduledExecutorService createScheduler() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(threadFactory);
        executorsToShutdown.add(executor);
        return executor;
    }

    public static ExecutorService createExecutorDaemon() {
        return Executors.newSingleThreadExecutor(daemanThreadFactory);
    }

    public static void shutDownAllExecutors() {
        executorsToShutdown.stream().forEach(ExecutorService::shutdown);
    }

    private static ThreadFactory threadFactory = r -> {
        Thread t = new Thread(r);
        return t;
    };

    private static ThreadFactory daemanThreadFactory = r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    };


}
