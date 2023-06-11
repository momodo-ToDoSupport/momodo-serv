package com.momodo.todohistory.batch;

import com.momodo.todo.repository.TodoRepository;
import com.momodo.todohistory.TodoHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TodoHistoryBatchConfig {

    @Bean
    public Tasklet todoHistoryTasklet(){
        return new TodoHistoryTasklet();
    }

    @Bean
    public Job todoHistoryJob(JobRepository jobRepository, Step todoHistoryStep) {
        return new JobBuilder("todoHistoryJob", jobRepository)
                .start(todoHistoryStep)
                .build();
    }

    @Bean
    public Step todoHistoryStep(JobRepository jobRepository, TodoHistoryTasklet todoHistoryTasklet
            , PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("todoHistoryStep", jobRepository)
                .tasklet(todoHistoryTasklet, platformTransactionManager)
                .build();
    }
}
