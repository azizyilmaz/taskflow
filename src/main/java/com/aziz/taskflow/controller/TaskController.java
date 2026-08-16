package com.aziz.taskflow.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @PostMapping
    public String executeTask(@RequestParam int taskCount) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < taskCount; i++) {
            System.out.println("Executing task " + (i + 1));
            try {
                Thread.sleep(1000); // Simulate task execution time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Task execution interrupted.";
            }
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        return "Executed " + taskCount + " tasks in " + elapsedTime + " milliseconds.";
    }
}
