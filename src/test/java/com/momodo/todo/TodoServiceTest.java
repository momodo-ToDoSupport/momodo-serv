package com.momodo.todo;

import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Test
    public void Todo_등록(){
        // given
        TodoRequestDto.Create todoRequest = todoRequestDto();
        Todo todo = todoRequest.toEntity();

        // when
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        Todo createdTodo = todoService.createTodo(todoRequest);

        // then
        assertThat(todo.getId()).isEqualTo(createdTodo.getId());
        assertThat(todo.getTitle()).isEqualTo(createdTodo.getTitle());
    }

    private TodoRequestDto.Create todoRequestDto(){
        return TodoRequestDto.Create.builder()
                .memberId(1L)
                .title("Test Todo")
                .emoji("Test Emoji")
                .dueDate(LocalDate.now())
                .repeatDays(null)
                .build();
    }
}
