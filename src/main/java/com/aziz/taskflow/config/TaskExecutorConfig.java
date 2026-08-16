package com.aziz.taskflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class TaskExecutorConfig {

    @Bean
    public ExecutorService createExecutorService() {
        int THREAD_POOL_SIZE = 10;
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }
}
