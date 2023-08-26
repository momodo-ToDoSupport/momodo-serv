package com.momodo.userApp.batch.job;

import com.momodo.todohistory.batch.tasks.TodoHistoryCreateTasklet;
import com.momodo.userApp.batch.tasks.TierResetTasklet;
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
public class TierResetJobConfig {

    @Bean
    public Tasklet tierResetTasklet(){
        return new TierResetTasklet();
    }

    @Bean
    public Job tierResetJob(JobRepository jobRepository, Step tierResetStep) {
        return new JobBuilder("tierResetJob", jobRepository)
                .start(tierResetStep)
                .build();
    }

    @JobScope
    @Bean
    public Step tierResetStep(JobRepository jobRepository, TierResetTasklet tierResetTasklet
            , PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("tierResetStep", jobRepository)
                .tasklet(tierResetTasklet, platformTransactionManager)
                .build();
    }
}
