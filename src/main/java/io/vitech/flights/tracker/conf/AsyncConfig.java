package io.vitech.flights.tracker.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        //TODO extract these values to application.yml
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // Set the core pool size
        executor.setMaxPoolSize(10);  // Set the maximum pool size
        executor.setQueueCapacity(25);  // Set the queue capacity
        executor.setThreadNamePrefix("sync-task-");
        executor.initialize();
        return executor;
    }
}
