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

    public Todo createTodo(TodoRequestDto.Create request){

        Todo createTodo = request.toEntity();

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

    public Todo updateCompleted(Long id, TodoRequestDto.EditCompleted request){

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        todo.updateCompleted(request.isCompleted());
        return todo;
    }

    public Todo update(Long id, TodoRequestDto.Edit request){

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        todo.update(request.getTitle(), request.getEmoji(), request.getRepeatDays());
        return todo;
    }

    public boolean deleteById(Long id){

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        todoRepository.delete(todo);
        return true;
    }
}
