package com.azizyilmaz.taskflow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TaskRunner {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<String>> futures = new ArrayList<>();

        try {
            for (int i = 1; i <= 10; i++) {
                final int id = i;
                futures.add(executor.submit(() -> {
                    String thread = Thread.currentThread().getName();
                    System.out.println("Task " + id + " started on " + thread);
                    try {
                        // Simulate work
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Task " + id + " was interrupted");
                        return "interrupted";
                    }
                    System.out.println("Task " + id + " finished");
                    return "ok";
                }));
            }

            // Wait for all tasks to complete
            for (Future<String> f : futures) {
                try {
                    f.get(); // block until the task completes
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Waiting was interrupted");
                } catch (ExecutionException e) {
                    System.err.println("Task failed: " + e.getCause());
                }
            }

            System.out.println("Completed 10 tasks");
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
