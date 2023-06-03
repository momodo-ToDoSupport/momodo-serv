package com.momodo.todo;

import com.momodo.todo.dto.TodoRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todos")
    public void createTodo(@RequestBody @Valid TodoRequestDto.Create requestDto){

        todoService.createTodo(requestDto);
    }
}
