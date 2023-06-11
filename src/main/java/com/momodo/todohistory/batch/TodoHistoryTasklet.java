package com.momodo.todohistory.batch;

import com.momodo.todo.Todo;
import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.dto.TodoResponseDto;
import com.momodo.todo.repository.TodoRepository;
import com.momodo.todohistory.TodoHistoryService;
import com.momodo.todohistory.dto.TodoHistoryRequestDto;
import com.momodo.todohistory.repository.TodoHistoryRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class TodoHistoryTasklet implements Tasklet, StepExecutionListener {

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private TodoHistoryService todoHistoryService;

    private List<TodoResponseDto.Info> todos;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LocalDate dueDate = LocalDate.now().minusDays(1);
        todos = todoRepository.findAllByDueDate(dueDate);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        if(todos.isEmpty()){
            return RepeatStatus.FINISHED;
        }

        Long count = (long)todos.size();
        Long completedCount = 0L;

        for(TodoResponseDto.Info t : todos){
            if(t.isCompleted())
                completedCount += 1;
        }

        TodoHistoryRequestDto.Create create = TodoHistoryRequestDto.Create.builder()
                .memberId(1L)
                .count(count)
                .completedCount(completedCount)
                .dueDate(todos.get(0).getDueDate())
                .build();

        todoHistoryService.create(create);

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
