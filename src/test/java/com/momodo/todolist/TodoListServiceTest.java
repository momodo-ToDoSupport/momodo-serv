package com.momodo.todolist;

import com.momodo.todolist.dto.TodoListResponseDto;
import com.momodo.todolist.repository.TodoListRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

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

    @Test
    @DisplayName("TodoList DueDate로 조회")
    public void findByDueDate(){
        // given
        Long memberId = 1L;
        LocalDate dueDate = LocalDate.now();
        TodoListResponseDto.Info info = createTodoList(memberId, dueDate).toInfo();

        // when
        when(todoListRepository.findByDueDate(memberId, dueDate)).thenReturn(info);
        TodoListResponseDto.Info result = todoListService.findByDueDate(memberId, dueDate);

        // then
        assertThat(info.getId()).isEqualTo(result.getId());
        assertThat(info.getDueDate()).isEqualTo(result.getDueDate());
    }

    @Test
    @DisplayName("TodoList 년월로 조회")
    public void findAllByYearAndMonth(){
        // given
        Long memberId = 1L;
        LocalDate date = LocalDate.parse("2023-06-01");
        LocalDate firstDate = date.withDayOfMonth(1);
        LocalDate lastDate = date.withDayOfMonth(date.lengthOfMonth());

        List<TodoListResponseDto.Info> infoList = createTodoLists();

        // when
        when(todoListRepository.findAllByYearMonth(memberId, firstDate, lastDate)).thenReturn(infoList);
        List<TodoListResponseDto.Info> result = todoListRepository.findAllByYearMonth(memberId, firstDate, lastDate);

        // then
        assertThat(result.size()).isEqualTo(infoList.size());
        assertThat(result.get(0).getDueDate().getYear()).isEqualTo(date.getYear());
        assertThat(result.get(0).getDueDate().getMonthValue()).isEqualTo(date.getMonthValue());
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

    private List<TodoListResponseDto.Info> createTodoLists(){
        TodoListResponseDto.Info info1 = new TodoListResponseDto.Info(1L, 1L, 0L, 0, LocalDate.parse("2023-06-02"));
        TodoListResponseDto.Info info2 = new TodoListResponseDto.Info(2L, 2L, 0L, 0, LocalDate.parse("2023-06-03"));
        TodoListResponseDto.Info info3 = new TodoListResponseDto.Info(3L, 3L, 0L, 0, LocalDate.parse("2023-06-04"));

        List<TodoListResponseDto.Info> infoList = List.of(
                info1, info2, info3
        );

        return infoList;
    }
}
