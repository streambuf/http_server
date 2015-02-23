package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

/**
 * Created by max on 22.02.15.
 */
class ExecutorTask extends Thread {

    private final Queue taskQueue;
    private static final Logger log = LoggerFactory.getLogger("service.ExecutorTask");

    ExecutorTask(Queue taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void run() {
        Runnable task;
        //noinspection InfiniteLoopStatement
        while (true) {
            synchronized(taskQueue) {
                while (taskQueue.isEmpty()) {
                    try {
                        taskQueue.wait();
                    } catch (InterruptedException e) {
                        log.debug(e.getMessage());
                    }
                }

                task = (Runnable) taskQueue.poll();
            }

            try {
                task.run();
            } catch (RuntimeException e) {
                log.error(e.getMessage());
            }
        }
    }
}
