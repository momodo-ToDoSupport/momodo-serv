package com.momodo.todo;

import com.momodo.emojihistory.EmojiHistoryService;
import com.momodo.todo.Todo;
import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.dto.TodoResponseDto;
import com.momodo.todo.event.TodoCreatedEvent;
import com.momodo.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void createTodo(TodoRequestDto.Create request){
        Todo createTodo = request.toEntity();

        todoRepository.save(createTodo);

        publisher.publishEvent(new TodoCreatedEvent(request.getMemberId(), request.getEmoji()));
    }

    @Transactional(readOnly = true)
    public TodoResponseDto.Info findById(Long id){
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        return todo.toInfo();
    }

    @Transactional(readOnly = true)
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

        if(request.getEmoji() != todo.getEmoji()){
            publisher.publishEvent(new TodoCreatedEvent(todo.getMemberId(), request.getEmoji()));
        }

        todo.update(request.getTitle(), request.getEmoji(), request.getRepeatDays());
    }

    @Transactional
    public void deleteById(Long id){
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        todoRepository.delete(todo);
    }
}
