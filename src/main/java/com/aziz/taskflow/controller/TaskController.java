package com.aziz.taskflow.controller;

import com.aziz.taskflow.service.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final ExecutorService executor;
    private final TaskService taskService;

    public TaskController(ExecutorService executor, TaskService taskService) {
        this.executor = executor;
        this.taskService = taskService;
    }

    @PostMapping
    public String executeTask(@RequestParam int taskCount) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < taskCount; i++) {
            int id = i + 1;
            futures.add(executor.submit(() -> taskService.executeTask(id)));
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            future.get(); // wait for the task to complete
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        return "Executed " + taskCount + " tasks in " + elapsedTime + " milliseconds.";
    }
}
