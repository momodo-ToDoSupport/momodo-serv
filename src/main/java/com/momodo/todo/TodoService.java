package com.momodo.todo;

import com.momodo.todo.Todo;
import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.dto.TodoResponseDto;
import com.momodo.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public Todo createTodo(TodoRequestDto.Create todoRequest){

        Todo createTodo = todoRequest.toEntity();

        return todoRepository.save(createTodo);
    }

    public TodoResponseDto.Info findById(Long id){

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        return todo.toInfo();
    }

    public List<TodoResponseDto.Info> findAllByDueDate(LocalDate dueDate){

        List<TodoResponseDto.Info> todoInfoList = todoRepository.findAllByDueDate(dueDate);

        return todoInfoList;
    }
}
