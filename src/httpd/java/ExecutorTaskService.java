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

    public ExecutorTaskService(int numExecutors) {
        this.numExecutors = numExecutors;

        for (int i = 0; i < numExecutors; ++i) {
            ExecutorTask executorTask = new ExecutorTask();
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

    private class ExecutorTask extends Thread {
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
                    System.out.println(e.getMessage());
                }
            }
        }

    }
}
