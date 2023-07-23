package com.momodo.todohistory;

import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import com.momodo.todohistory.repository.TodoHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
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
    @DisplayName("TodoHistory Bulk Save")
    public void createAll(){
        // given
        List<TodoHistory> todoHistories = Collections.EMPTY_LIST;

        // when
        todoHistoryService.createAll(todoHistories);

        // then
        verify(todoHistoryRepository, times(1)).saveAll(any(List.class));
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
        List<TodoHistoryResponseDto.Info> infoList = createInfoList();

        // stub
        when(todoHistoryRepository.findAllByYearMonth(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(infoList);

        // when
        List<TodoHistoryResponseDto.Info> result = todoHistoryService.findAllByYearMonth(memberId, yearMonth);

        // then
        assertThat(result.size()).isEqualTo(infoList.size());
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

    private List<TodoHistoryResponseDto.Info> createInfoList(){
        TodoHistoryResponseDto.Info info1 = TodoHistoryResponseDto.Info.builder()
                .id(1L)
                .count(1L)
                .completedCount(0L)
                .step(0)
                .dueDate(LocalDate.parse("2023-06-02"))
                .build();
        TodoHistoryResponseDto.Info info2 = TodoHistoryResponseDto.Info.builder()
                .id(2L)
                .count(2L)
                .completedCount(0L)
                .step(0)
                .dueDate(LocalDate.parse("2023-06-03"))
                .build();
        TodoHistoryResponseDto.Info info3 = TodoHistoryResponseDto.Info.builder()
                .id(3L)
                .count(3L)
                .completedCount(0L)
                .step(0)
                .dueDate(LocalDate.parse("2023-06-04"))
                .build();

        List<TodoHistoryResponseDto.Info> infoList = List.of(
                info1, info2, info3
        );

        return infoList;
    }
}
