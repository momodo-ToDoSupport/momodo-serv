package com.momodo.todohistory.batch.tasks;

import com.momodo.todo.Todo;
import com.momodo.todo.repository.TodoRepository;
import com.momodo.todohistory.TodoHistoryService;
import com.momodo.todohistory.domain.TodoHistory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TodoHistoryCreateTasklet implements Tasklet, StepExecutionListener {

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private TodoHistoryService todoHistoryService;

    private LocalDate dueDate;
    private List<Todo> todos;
    private Map<String, List<Todo>> groupingByMember;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // 이전 날에 해당하는 Todo들을 모두 조회하여 사용자마다 TodoHistory를 생성
        dueDate = LocalDate.now().minusDays(1);
        todos = todoRepository.findAllByDueDate(dueDate);

        // memberId를 기준으로 그룹화
        groupingByMember = todos.stream()
                .collect(Collectors.groupingBy(t -> t.getMemberId()));
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        if(todos.isEmpty()){
            return RepeatStatus.FINISHED;
        }

        List<TodoHistory> saveList = new ArrayList<>();

        // member 별로 TodoHistory를 생성하여 saveList에 추가
        groupingByMember.forEach((key, todos) -> {
            Long count = (long)todos.size();
            Long completedCount = 0L;

            for(Todo t : todos){
                if(t.isCompleted())
                    completedCount += 1;
            }

            Integer step = todoHistoryService.calculateStep(count, completedCount);

            TodoHistory todoHistory = TodoHistory.builder()
                    .memberId(key)
                    .count(count)
                    .completedCount(completedCount)
                    .step(step)
                    .dueDate(dueDate)
                    .build();

            saveList.add(todoHistory);
        });

        todoHistoryService.createAll(saveList);

        return RepeatStatus.FINISHED;
    }
}
