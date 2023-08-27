package com.momodo.quartz;

import com.momodo.todohistory.batch.job.TodoHistoryCreateJob;
import com.momodo.userApp.batch.job.TierResetScheduledJob;
import org.quartz.*;
import org.springframework.batch.core.configuration.JobLocator;
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

        // 매일 00시 00분 01초에 실행
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule("1 0 0 * * ?");

//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
//                .simpleSchedule()
//                .withIntervalInHours(1)
//                .repeatForever();

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

        return JobBuilder.newJob(TierResetScheduledJob.class)
                .withIdentity("tierResetJob")
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger tierResetJobTrigger(){
        // 매월 1일 자정에 실행
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule("0 0 0 1 * ?");

        /*
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInHours(1)
                .repeatForever();*/

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
        scheduler.setJobDetails(tierResetJobDetail(), todoHistoryCreateJobDetail());
        scheduler.setTriggers(tierResetJobTrigger(), todoHistoryCreateJobTrigger());
        return scheduler;
    }
}
