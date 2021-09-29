package executor;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {

    public static int numberOfThreads = 5;

    private static Executor executorService;
    public ExecutorService threadPool;

    private Executor() {
        this.threadPool = Executors.newFixedThreadPool(numberOfThreads);
    }

    public static Executor getInstance() {
        if (executorService == null) {
            executorService = new Executor();
        }
        return executorService;
    }

    public static HashData runCalculateHashTasks(String magicString, int hashComplexity) {
        ArrayList<CalculateHashTask> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            tasks.add(new CalculateHashTask(magicString, hashComplexity));
        }

        try {
            return getInstance().threadPool.invokeAny(tasks);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
