package com.aziz.taskflow.controller;

import com.aziz.taskflow.service.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final ThreadPoolExecutor executor;
    private final TaskService taskService;

    public TaskController(ThreadPoolExecutor executor, TaskService taskService) {
        this.executor = executor;
        this.taskService = taskService;
    }

    @PostMapping
    public String executeTask(@RequestParam int taskCount) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

        List<Future<?>> futures = new ArrayList<>();
        System.out.println("========================================");
        System.out.println("REQUEST STARTED | taskCount=" + taskCount);
        System.out.println("Pool Size      = " + executor.getPoolSize());
        System.out.println("Active Threads = " + executor.getActiveCount());
        System.out.println("Queue Size     = " + executor.getQueue().size());
        System.out.println("========================================");

        for (int i = 0; i < taskCount; i++) {
            int id = i + 1;
            futures.add(executor.submit(() -> {
                System.out.println("TASK " + id + " STARTED | thread=" + Thread.currentThread().getName() + " | active=" + executor.getActiveCount() + " | queue=" + executor.getQueue().size());
                taskService.executeTask(id);
                System.out.println("TASK " + id + " FINISHED | active=" + executor.getActiveCount() + " | queue=" + executor.getQueue().size());
            }));
            System.out.println("TASK " + id + " SUBMITTED | active=" + executor.getActiveCount() + " | queue=" + executor.getQueue().size());
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            future.get(); // wait for the task to complete
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("========================================");
        System.out.println("REQUEST FINISHED | taskCount=" + taskCount + " | elapsed=" + elapsedTime + " ms");
        System.out.println("Active Threads = " + executor.getActiveCount());
        System.out.println("Queue Size     = " + executor.getQueue().size());
        System.out.println("========================================");
        return "Executed " + taskCount + " tasks in " + elapsedTime + " milliseconds.";
    }
}
