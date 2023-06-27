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
    @Transactional
    @DisplayName("Todo 등록")
    public void create(){
        // given
        TodoRequestDto.Create todoRequest = todoRequestDto();
        Todo todo = todoRequest.toEntity();

        // when
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        todoService.createTodo(todoRequest, memberId);

        // then
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

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
        List<TodoResponseDto.Info> infoList = createInfoList();

        // when
        when(todoRepository.findAllByDueDate(any(LocalDate.class))).thenReturn(infoList);
        List<TodoResponseDto.Info> result = todoService.findAllByDueDate(date);

        // then
        assertThat(result.size()).isEqualTo(infoList.size());
        assertThat(infoList.get(0).getDueDate()).isEqualTo(date);
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
    @DisplayName("Todo 정보 수정")
    public void update(){
        // given
        Todo foundTodo = createTodo();
        Long id = 1L;
        TodoRequestDto.Edit request = new TodoRequestDto.Edit("Edit Title", "Edit Emoji", "Edit RepeatDays");

        // when
        when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(foundTodo));
        todoService.update(id, request);

        // then
        assertThat(foundTodo.getTitle()).isEqualTo(request.getTitle());
        assertThat(foundTodo.getEmoji()).isEqualTo(request.getEmoji());
        assertThat(foundTodo.getRepeatDays()).isEqualTo(request.getRepeatDays());
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

    private List<TodoResponseDto.Info> createInfoList() {

        TodoResponseDto.Info info1 = TodoResponseDto.Info.builder()
                .id(1L)
                .title("todo1")
                .emoji("emoji1")
                .dueDate(LocalDate.now())
                .isCompleted(false)
                .repeatDays(null)
                .build();
        TodoResponseDto.Info info2 = TodoResponseDto.Info.builder()
                .id(2L)
                .title("todo2")
                .emoji("emoji2")
                .dueDate(LocalDate.now())
                .isCompleted(false)
                .repeatDays(null)
                .build();

        List<TodoResponseDto.Info> infoList = List.of(
                info1, info2
        );

        return infoList;
    }
}
