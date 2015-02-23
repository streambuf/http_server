package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by max on 20.02.15.
 */
public class ExecutorTaskService {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<ExecutorTask> executors = new ArrayList<>();
    private final Queue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    public ExecutorTaskService(@SuppressWarnings("SameParameterValue") int numExecutors) {

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

}
