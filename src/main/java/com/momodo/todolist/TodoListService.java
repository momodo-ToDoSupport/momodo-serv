package com.momodo.todolist;

import com.momodo.todolist.dto.TodoListResponseDto;
import com.momodo.todolist.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoListService {

    private final TodoListRepository todoListRepository;

    public TodoList create(Long memberId, LocalDate dueDate){

        TodoList todoList = TodoList.builder()
                .memberId(memberId)
                .count(1L)
                .completedCount(0L)
                .step(0)
                .dueDate(dueDate)
                .build();

        TodoList createdTodoList = todoListRepository.save(todoList);

        return createdTodoList;
    }

    public TodoListResponseDto.Info findByDueDate(Long memberId, LocalDate dueDate) {

        return todoListRepository.findByDueDate(memberId, dueDate);
    }

    public List<TodoListResponseDto.Info> findAllByYearMonth(Long memberId, String yearMonth) {

        LocalDate firstDate = LocalDate.parse(yearMonth + "-01");
        LocalDate lastDate = firstDate.withDayOfMonth(firstDate.lengthOfMonth());

        return todoListRepository.findAllByYearMonth(memberId, firstDate, lastDate);
    }
}
