package com.momodo.userApp.batch.tasks;

import com.momodo.userApp.service.UserAppService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class TierResetTasklet implements Tasklet {

    @Autowired
    private UserAppService userAppService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        userAppService.resetAllUserTiers();

        return RepeatStatus.FINISHED;
    }
}
