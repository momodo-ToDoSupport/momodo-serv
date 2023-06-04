package com.momodo.todo;

import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.dto.TodoResponseDto;
import com.momodo.todo.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Test
    @DisplayName("Todo 등록")
    public void create(){
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

    @Test
    @DisplayName("Todo Id로 조회")
    public void findById(){
        // given
        Long id = 1L;
        Todo savedTodo = createTodo();

        // when
        when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(savedTodo));
        TodoResponseDto.Info todoInfo = todoService.findById(id);

        // then
        assertThat(savedTodo.getId()).isEqualTo(todoInfo.getId());
        assertThat(savedTodo.getTitle()).isEqualTo(todoInfo.getTitle());
    }

    @Test
    @DisplayName("Todo DueDate로 조회")
    public void findAllByDueDate(){
        // given
        LocalDate date = LocalDate.now();
        TodoResponseDto.Info todoInfo1 = new TodoResponseDto.Info(1L, "todo1", "emoji1", date, false, null);
        TodoResponseDto.Info todoInfo2 = new TodoResponseDto.Info(2L, "todo2", "emoji2", date, false, null);
        List<TodoResponseDto.Info> todoInfoList = List.of(todoInfo1, todoInfo2);

        // when
        when(todoRepository.findAllByDueDate(any(LocalDate.class))).thenReturn(todoInfoList);
        List<TodoResponseDto.Info> result = todoRepository.findAllByDueDate(date);

        // then
        assertThat(result.size()).isEqualTo(todoInfoList.size());
        assertThat(todoInfoList.get(0).getDueDate()).isEqualTo(date);
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

    private Todo createTodo(){
        return Todo.builder()
                .memberId(1L)
                .title("Test Todo")
                .emoji("Test Emoji")
                .dueDate(LocalDate.now())
                .isCompleted(false)
                .repeatDays(null)
                .build();
    }
}
