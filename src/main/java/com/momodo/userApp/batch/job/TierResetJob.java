package com.momodo.userApp.batch.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TierResetJob extends QuartzJobBean {

    private String jobName;
    private JobLauncher jobLauncher;
    private JobLocator jobLocator;

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    public void setJobLocator(JobLocator jobLocator) {
        this.jobLocator = jobLocator;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        try{
            Job job = jobLocator.getJob(jobName);
            JobParameters params = new JobParametersBuilder()
                    .addString("JobId", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();

            jobLauncher.run(job, params);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
