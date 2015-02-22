package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ExecutorTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by max on 20.02.15.
 */
public class ExecutorTaskService {

    private final List<ExecutorTask> executors = new ArrayList<ExecutorTask>();
    private final Queue taskQueue = new LinkedBlockingQueue();
    private final int numExecutors;
    private static Logger log = LoggerFactory.getLogger("service.ExecutorTaskService");

    public ExecutorTaskService(int numExecutors) {
        this.numExecutors = numExecutors;

        for (int i = 0; i < numExecutors; ++i) {
            ExecutorTask executorTask = new ExecutorTask(taskQueue);
            executors.add(executorTask);
            executorTask.start();
        }
    }

    public void execute(Runnable task) {
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify();
        }
    }

    public int getNumExecutors() {
        return numExecutors;
    }

    public Queue getTaskQueue() {
        return taskQueue;
    }
}
