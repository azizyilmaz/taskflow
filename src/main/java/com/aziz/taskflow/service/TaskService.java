package com.aziz.taskflow.service;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class TaskService {

    private final DataSource dataSource;

    public TaskService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeTask(int taskId) {
        try (Connection connection = dataSource.getConnection()) {
            // Simulate task execution
            System.out.println("Executing task " + taskId + " GOT CONNECTION on " + Thread.currentThread().getName());
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO task_execution(task_name) VALUES (?)")) {
                statement.setString(1, "task-" + taskId);
                statement.executeUpdate();
            }
            Thread.sleep(1000); // Simulate a task taking 1000 milliseconds
            System.out.println("Finished task " + taskId + " RELEASING CONNECTION on " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
