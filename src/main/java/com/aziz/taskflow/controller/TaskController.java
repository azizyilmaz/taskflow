package com.aziz.taskflow.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @PostMapping
    public String executeTask(@RequestParam int taskCount) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < taskCount; i++) {
            int id = i + 1;
            futures.add(executor.submit(() -> {
                System.out.println("Executing task " + id + " on " + Thread.currentThread().getName());
                try {
                    // Simulate task execution
                    Thread.sleep(1000); // Simulate a task taking 1000 milliseconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Interrupted task " + id + " on " + Thread.currentThread().getName());
                }
            }));
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            future.get(); // wait for the task to complete
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        return "Executed " + taskCount + " tasks in " + elapsedTime + " milliseconds.";
    }
}
