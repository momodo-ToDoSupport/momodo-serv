package com.momodo.todohistory.batch.tasks;

import com.momodo.todohistory.TodoHistoryService;
import com.momodo.userApp.domain.Tier;
import com.momodo.userApp.service.UserAppService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Map;

public class TodoTierUpdateTasklet implements Tasklet, StepExecutionListener {

    @Autowired
    private TodoHistoryService todoHistoryService;
    @Autowired
    private UserAppService userAppService;

    // 전날
    private LocalDate beforeDate;
    private boolean isLastDate;
    private Map<String, Long> groupingByMember;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // StepExecutionListener.super.beforeStep(stepExecution);
        beforeDate = LocalDate.now().minusDays(1);

        // 전날이 달의 마지막 날이였다면 실행 X
        isLastDate = beforeDate.isEqual(beforeDate.withDayOfMonth(beforeDate.lengthOfMonth()));

        if(isLastDate){
            return;
        }

        groupingByMember = todoHistoryService.countBySecondStepAchievement(beforeDate);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        if(isLastDate){
            return RepeatStatus.FINISHED;
        }

        if(groupingByMember.isEmpty()) {
            return RepeatStatus.FINISHED;
        }

        /**
         * 월 1일 부터 beforeDate까지
         * 유저별 2단계 이상 달성한 TodoHistory 개수 정보를 통해 TodoTier 계산 후 수정
         */
        groupingByMember.forEach((key, count) -> {
            Tier tier = todoHistoryService.calculateTodoTier(count);

            userAppService.updateTier(key, tier);
        });

        return RepeatStatus.FINISHED;
    }
}
