package com.momodo.config;

import com.momodo.todohistory.batch.job.TodoHistoryCreateJob;
import com.momodo.userApp.batch.job.TierResetJob;
import com.momodo.userApp.domain.Tier;
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
    public JobDetail todoHistoryCreateJobDetail(){
        // Set Job Data Map
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "todoHistoryCreateJob");
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobLocator);

        return JobBuilder.newJob(TodoHistoryCreateJob.class)
                .withIdentity("todoHistoryCreateJob")
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger todoHistoryCreateJobTrigger(){
        /*
        // 매일 자정에 실행
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule("0 0 0 * * ?");*/

        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInHours(1)
                .repeatForever();

        return TriggerBuilder
                .newTrigger()
                .forJob(todoHistoryCreateJobDetail())
                .withIdentity("todoHistoryCreateJobTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail tierResetJobDetail(){
        // Set Job Data Map
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "tierResetJob");
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobLocator);

        return JobBuilder.newJob(TierResetJob.class)
                .withIdentity("tierResetJob")
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger tierResetJobTrigger(){
        /*
        // 매일 자정에 실행
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule("0 0 0 * * ?");*/

        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInHours(1)
                .repeatForever();

        return TriggerBuilder
                .newTrigger()
                .forJob(tierResetJobDetail())
                .withIdentity("tierResetJobTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws SchedulerException {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTriggers(tierResetJobTrigger());
        scheduler.setJobDetails(tierResetJobDetail());

        return scheduler;
    }
}
