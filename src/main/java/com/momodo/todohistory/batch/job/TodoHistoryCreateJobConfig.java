package com.momodo.todohistory.batch.job;

import com.momodo.todohistory.batch.tasks.TodoHistoryCreateTasklet;
import com.momodo.todohistory.batch.tasks.TodoTierUpdateTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
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
public class TodoHistoryCreateJobConfig {

    @Bean
    public Tasklet todoHistoryCreateTasklet(){
        return new TodoHistoryCreateTasklet();
    }

    @Bean
    public Tasklet todoTierUpdateTasklet(){
        return new TodoTierUpdateTasklet();
    }

    @Bean
    public Job todoHistoryCreateJob(JobRepository jobRepository, Step todoHistoryCreateStep, Step todoTierUpdateStep) {
        return new JobBuilder("todoHistoryCreateJob", jobRepository)
                .start(todoHistoryCreateStep)
                .next(todoTierUpdateStep)
                .build();
    }

    @JobScope
    @Bean
    public Step todoHistoryCreateStep(JobRepository jobRepository, TodoHistoryCreateTasklet todoHistoryCreateTasklet
            , PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("todoHistoryCreateStep", jobRepository)
                .tasklet(todoHistoryCreateTasklet, platformTransactionManager)
                .build();
    }

    @JobScope
    @Bean
    public Step todoTierUpdateStep(JobRepository jobRepository, TodoTierUpdateTasklet todoTierUpdateTasklet
            , PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("todoTierUpdateStep", jobRepository)
                .tasklet(todoTierUpdateTasklet, platformTransactionManager)
                .build();
    }
}
