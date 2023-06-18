package com.momodo.todohistory.batch.config;

import com.momodo.todohistory.batch.job.TodoHistoryQuartzJob;
import org.quartz.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobLocator jobLocator;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry){
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);

        return  jobRegistryBeanPostProcessor;
    }

    @Bean
    public JobDetail todoHistoryJobDetail(){
        // Set Job Data Map
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "todoHistoryJob");
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobLocator);

        return JobBuilder.newJob(TodoHistoryQuartzJob.class)
                .withIdentity("todoHistoryJob")
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger todoHistoryJobTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInHours(1)
                .repeatForever();

        return TriggerBuilder
                .newTrigger()
                .forJob(todoHistoryJobDetail())
                .withIdentity("todoHistoryJobTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTriggers(todoHistoryJobTrigger());
        scheduler.setJobDetails(todoHistoryJobDetail());

        return scheduler;
    }
}
