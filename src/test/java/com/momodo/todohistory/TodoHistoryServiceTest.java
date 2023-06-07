package com.momodo.todohistory;

import com.momodo.todo.Todo;
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

    @Test
    @DisplayName("Todo 등록")
    public void create(){
        // given
        TodoHistory todoHistory = createTodoHistory();

        // stub
        when(todoHistoryRepository.save(any(TodoHistory.class))).thenReturn(todoHistory);

        // when
        todoHistoryService.create(todoHistory.getMemberId(), todoHistory.getDueDate());

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
        Long memberId = 1L;
        String yearMonth = "2023-06";
        List<TodoHistoryResponseDto.Info> infoList = createTodoHistorys();

        // stub
        when(todoHistoryRepository.findAllByYearMonth(any(Long.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(infoList);

        // when
        List<TodoHistoryResponseDto.Info> result = todoHistoryService.findAllByYearMonth(memberId, yearMonth);

        // then
        assertThat(result.size()).isEqualTo(infoList.size());
    }

    //region TodoHistory의 Todo 개수 수정 테스트
    @Test
    @DisplayName("TodoHistory의 Todo 개수 수정 - Todo가 생성 됐을 때, DueDate 날짜의 TodoHistory 데이터가 없을 경우")
    public void updateTodoCount_NotFoundTodoHistory(){
        // given
        Todo todo = createTodo();
        boolean isCreated = true;
        TodoHistory todoHistory = createTodoHistory();

        // stub
        when(todoHistoryRepository.findByDueDate(any(Long.class), any(LocalDate.class))).thenReturn(null);
        when(todoHistoryRepository.save(any(TodoHistory.class))).thenReturn(todoHistory);

        // when
        todoHistoryService.updateTodoCount(todo, isCreated);

        // then
        verify(todoHistoryRepository, times(1)).save(any(TodoHistory.class));
    }

    @Test
    @DisplayName("TodoHistory의 Todo 개수 수정 - Todo가 생성 됐을 때, DueDate 날짜의 TodoHistory 데이터가 존재할 경우")
    public void updateTodoCount_ExistsTodoHistory(){
        // given
        Todo todo = createTodo();
        boolean isCreated = true;

        // 3개의 Todo가 존재하고 전부 완료한 상태
        TodoHistory todoHistory = new TodoHistory(1L, 3L, 3L, 3, LocalDate.now());

        // stub
        when(todoHistoryRepository.findByDueDate(any(Long.class), any(LocalDate.class))).thenReturn(todoHistory);

        // when
        todoHistoryService.updateTodoCount(todo, isCreated);

        // then
        assertThat(todoHistory.getCount()).isEqualTo(4L); // Todo가 추가되었으니 4
        assertThat(todoHistory.getCompletedCount()).isEqualTo(3L);
        assertThat(todoHistory.getStep()).isEqualTo(2); // 4개 중에 3개가 완료됐으니 75%로 2단계
    }

    @Test
    @DisplayName("TodoHistory의 Todo 개수 수정 - Todo가 삭제 됐을 때, DueDate 날짜의 Todo 데이터가 없을 경우")
    public void updateTodoCount_TodoCountZero(){
        // given
        Todo todo = createTodo();
        boolean isCreated = false; // 삭제할 것이기 때문에 false

        // 1개의 Todo가 존재
        TodoHistory todoHistory = new TodoHistory(1L, 1L, 0L, 0, LocalDate.now());

        // stub
        when(todoHistoryRepository.findByDueDate(any(Long.class), any(LocalDate.class))).thenReturn(todoHistory);

        // when
        todoHistoryService.updateTodoCount(todo, isCreated);

        // then
        // Todo의 개수가 0개가 되었기 때문에 TodoHistory를 삭제
        verify(todoHistoryRepository, times(1)).delete(any(TodoHistory.class));
    }

    @Test
    @DisplayName("TodoHistory의 Todo 개수 수정 - Todo가 삭제 됐을 때, DueDate 날짜의 Todo 데이터가 존재할 경우")
    public void updateTodoCount_TodoExists(){
        // given
        Todo todo = createTodo();
        boolean isCreated = false; // 삭제할 것이기 때문에 false

        // 2개의 Todo가 존재
        TodoHistory todoHistory = new TodoHistory(1L, 2L, 0L, 0, LocalDate.now());

        // stub
        when(todoHistoryRepository.findByDueDate(any(Long.class), any(LocalDate.class))).thenReturn(todoHistory);

        // when
        todoHistoryService.updateTodoCount(todo, isCreated);

        // then
        assertThat(todoHistory.getCount()).isEqualTo(1L); // Todo 1개가 삭제되었으니
    }
    //endregion

    //region TodoHistory의 Todo 완료 개수 수정
    @Test
    @DisplayName("TodoHistory의 Todo 완료 개수 수정 - Todo가 미완료에서 완료로")
    public void updateTodoCompleted_미완료에서완료로(){
        // given
        Todo todo = createTodo();
        todo.updateCompleted(); // isCompleted - true로 변경

        // 3개의 Todo가 존재하고 전부 완료한 상태
        TodoHistory todoHistory = new TodoHistory(1L, 3L, 2L, 3, LocalDate.now());

        // stub
        when(todoHistoryRepository.findByDueDate(any(Long.class), any(LocalDate.class))).thenReturn(todoHistory);

        // when
        todoHistoryService.updateTodoCompleted(todo);

        // then
        assertThat(todoHistory.getCompletedCount()).isEqualTo(3L);
        assertThat(todoHistory.getStep()).isEqualTo(3); // 3개 중에 3개가 완료됐으니 100%로 3단계
    }

    @Test
    @DisplayName("TodoHistory의 Todo 완료 개수 수정 - Todo가 완료에서 미완료로")
    public void updateTodoCompleted_완료에서미완료로(){
        // given
        Todo todo = createTodo();

        // 3개의 Todo가 존재하고 전부 완료한 상태
        TodoHistory todoHistory = new TodoHistory(1L, 3L, 3L, 3, LocalDate.now());

        // stub
        when(todoHistoryRepository.findByDueDate(any(Long.class), any(LocalDate.class))).thenReturn(todoHistory);

        // when
        todoHistoryService.updateTodoCompleted(todo);

        // then
        assertThat(todoHistory.getCompletedCount()).isEqualTo(2L);
        assertThat(todoHistory.getStep()).isEqualTo(1); // 3개 중에 2개가 완료됐으니 66%로 1단계
    }
    //endregion

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

    private TodoHistory createTodoHistory(){
        return TodoHistory.builder()
                .memberId(1L)
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
