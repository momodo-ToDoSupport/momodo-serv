package com.momodo.todolist;

import com.momodo.todolist.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
}
