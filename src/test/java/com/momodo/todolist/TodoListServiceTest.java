package com.momodo.todolist;

import com.momodo.todolist.repository.TodoListRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoListServiceTest {

    @InjectMocks
    private TodoListService todoListService;

    @Mock
    private TodoListRepository todoListRepository;

    @Test
    @DisplayName("Todo 등록")
    public void create(){
        // given
        Long memberId = 1L;
        LocalDate dueDate = LocalDate.now();
        TodoList todoList = createTodoList(memberId, dueDate);

        // when
        when(todoListRepository.save(any(TodoList.class))).thenReturn(todoList);
        TodoList createdTodoList = todoListService.create(memberId, dueDate);

        // then
        assertThat(createdTodoList.getId()).isEqualTo(todoList.getId());
        assertThat(createdTodoList.getMemberId()).isEqualTo(todoList.getMemberId());
    }

    private TodoList createTodoList(Long memberId, LocalDate dueDate){
        return TodoList.builder()
                .memberId(memberId)
                .count(1L)
                .completedCount(0L)
                .step(0)
                .dueDate(dueDate)
                .build();
    }
}
