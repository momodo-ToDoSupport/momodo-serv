package com.momodo.todo;

import com.momodo.todo.Todo;
import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public Todo createTodo(TodoRequestDto.Create todoRequest){

        Todo createTodo = todoRequest.toEntity();

        return todoRepository.save(createTodo);
    }
}
