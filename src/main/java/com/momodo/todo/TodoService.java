package com.momodo.todo;

import com.momodo.todo.Todo;
import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.dto.TodoResponseDto;
import com.momodo.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    @Transactional
    public void createTodo(TodoRequestDto.Create request){

        Todo createTodo = request.toEntity();

        todoRepository.save(createTodo);
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

    @Transactional
    public void updateCompleted(Long id){

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        todo.updateCompleted();
    }

    @Transactional
    public void update(Long id, TodoRequestDto.Edit request){

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        todo.update(request.getTitle(), request.getEmoji(), request.getRepeatDays());
    }

    @Transactional
    public void deleteById(Long id){

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        todoRepository.delete(todo);
    }
}
