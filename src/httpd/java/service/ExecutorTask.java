package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

/**
 * Created by max on 22.02.15.
 */
public class ExecutorTask extends Thread {

    private final Queue taskQueue;
    private static Logger log = LoggerFactory.getLogger("service.ExecutorTask");

    ExecutorTask(Queue taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void run() {
        Runnable task;
        while (true) {
            synchronized(taskQueue) {
                while (taskQueue.isEmpty()) {
                    try {
                        taskQueue.wait();
                    } catch (InterruptedException e) {
                        continue;
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
