package com.momodo.todo;

import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.dto.TodoResponseDto;
import com.momodo.todo.event.TodoCreatedEvent;
import com.momodo.todo.repository.TodoRepository;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

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

    private String memberId = "Test";

    @Test
    @DisplayName("Todo Id로 조회")
    public void findById(){
        // given
        Long id = 1L;
        Todo foundTodo = createTodo();

        // when
        when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(foundTodo));
        TodoResponseDto.Info todoInfo = todoService.findById(id);

        // then
        assertThat(foundTodo.getId()).isEqualTo(todoInfo.getId());
        assertThat(foundTodo.getTitle()).isEqualTo(todoInfo.getTitle());
    }

    @Test
    @DisplayName("Todo DueDate로 조회")
    public void findAllByDueDate(){
        // given
        LocalDate date = LocalDate.now();
        List<Todo> todoList = createInfoList();

        // when
        when(todoRepository.findAllByDueDate(any(LocalDate.class))).thenReturn(todoList);
        List<Todo> result = todoService.findAllByDueDate(date);

        // then
        assertThat(result.size()).isEqualTo(todoList.size());
        assertThat(todoList.get(0).getDueDate()).isEqualTo(date);
    }

    @Test
    @Transactional
    @DisplayName("Todo 성공 여부 수정")
    public void updateCompleted(){
        // given
        Todo foundTodo = createTodo();
        Long id = 1L;

        // when
        when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(foundTodo));
        todoService.updateCompleted(id);

        // then
        assertThat(foundTodo.isCompleted()).isTrue();
    }


    @Test
    @Transactional
    @DisplayName("Todo 삭제")
    public void deleteById(){
        // given
        Todo foundTodo = createTodo();
        Long id = 1L;

        // when
        when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(foundTodo));
        todoService.deleteById(id);

        // then
        verify(todoRepository, times(1)).delete(any(Todo.class));
    }

    private TodoRequestDto.Create todoRequestDto(){
        return TodoRequestDto.Create.builder()
                .title("Test Todo")
                .emoji("Test Emoji")
                .dueDate(LocalDate.now())
                .repeatDays(null)
                .build();
    }

    private Todo createTodo(){
        return Todo.builder()
                .memberId(memberId)
                .title("Test Todo")
                .emoji("Test Emoji")
                .dueDate(LocalDate.now())
                .isCompleted(false)
                .repeatDays(null)
                .build();
    }

    private List<Todo> createInfoList() {

        Todo todo1 = Todo.builder()
                .id(1L)
                .title("todo1")
                .emoji("emoji1")
                .memberId(memberId)
                .dueDate(LocalDate.now())
                .isCompleted(false)
                .repeatDays(null)
                .build();
        Todo todo2 = Todo.builder()
                .id(1L)
                .title("todo2")
                .emoji("emoji2")
                .memberId(memberId)
                .dueDate(LocalDate.now())
                .isCompleted(false)
                .repeatDays(null)
                .build();

        List<Todo> todoList = List.of(
                todo1, todo2
        );

        return todoList;
    }
}
