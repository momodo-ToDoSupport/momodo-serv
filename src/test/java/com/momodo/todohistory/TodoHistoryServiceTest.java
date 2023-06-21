package com.momodo.todohistory;

import com.momodo.todo.Todo;
import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todohistory.dto.TodoHistoryRequestDto;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import com.momodo.todohistory.repository.TodoHistoryRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoHistoryServiceTest {

    @InjectMocks
    private TodoHistoryService todoHistoryService;

    @Mock
    private TodoHistoryRepository todoHistoryRepository;

    private String memberId = "Test";

    @Test
    @DisplayName("Todo 등록")
    public void create(){
        // given
        TodoHistory todoHistory = createTodoHistory();
        TodoHistoryRequestDto.Create request = TodoHistoryRequestDto.Create.builder()
                .memberId(todoHistory.getMemberId())
                .count(todoHistory.getCount())
                .completedCount(todoHistory.getCompletedCount())
                .dueDate(todoHistory.getDueDate())
                .build();

        // stub
        when(todoHistoryRepository.save(any(TodoHistory.class))).thenReturn(todoHistory);

        // when
        todoHistoryService.create(request);

        // then
        verify(todoHistoryRepository, times(1)).save(any(TodoHistory.class));
    }

    @Test
    @DisplayName("TodoHistory DueDate로 조회")
    public void findByDueDate(){
        // given
        TodoHistory todoHistory = createTodoHistory();

        // stub
        when(todoHistoryRepository.findByDueDate(todoHistory.getMemberId(), todoHistory.getDueDate())).thenReturn(todoHistory);

        // when
        TodoHistoryResponseDto.Info result = todoHistoryService.findByDueDate(todoHistory.getMemberId(), todoHistory.getDueDate());

        // then
        assertThat(todoHistory.getId()).isEqualTo(result.getId());
        assertThat(todoHistory.getDueDate()).isEqualTo(result.getDueDate());
    }

    @Test
    @DisplayName("TodoHistory 년월로 조회")
    public void findAllByYearAndMonth(){
        // given
        String yearMonth = "2023-06";
        List<TodoHistoryResponseDto.Info> infoList = createTodoHistorys();

        // stub
        when(todoHistoryRepository.findAllByYearMonth(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(infoList);

        // when
        List<TodoHistoryResponseDto.Info> result = todoHistoryService.findAllByYearMonth(memberId, yearMonth);

        // then
        assertThat(result.size()).isEqualTo(infoList.size());
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

    private TodoHistory createTodoHistory(){
        return TodoHistory.builder()
                .memberId(memberId)
                .count(1L)
                .completedCount(0L)
                .step(0)
                .dueDate(LocalDate.now())
                .build();
    }

    private List<TodoHistoryResponseDto.Info> createTodoHistorys(){
        TodoHistoryResponseDto.Info info1 = new TodoHistoryResponseDto.Info(1L, 1L, 0L, 0, LocalDate.parse("2023-06-02"));
        TodoHistoryResponseDto.Info info2 = new TodoHistoryResponseDto.Info(2L, 2L, 0L, 0, LocalDate.parse("2023-06-03"));
        TodoHistoryResponseDto.Info info3 = new TodoHistoryResponseDto.Info(3L, 3L, 0L, 0, LocalDate.parse("2023-06-04"));

        List<TodoHistoryResponseDto.Info> infoList = List.of(
                info1, info2, info3
        );

        return infoList;
    }
}
