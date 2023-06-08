package com.momodo.todolist;

import com.momodo.todo.Todo;
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
import static org.mockito.Mockito.*;

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
        TodoList todoList = createTodoList();

        // stub
        when(todoListRepository.save(any(TodoList.class))).thenReturn(todoList);

        // when
        todoListService.create(todoList.getMemberId(), todoList.getDueDate());

        // then
        verify(todoListRepository, times(1)).save(any(TodoList.class));
    }

    @Test
    @DisplayName("TodoList DueDate로 조회")
    public void findByDueDate(){
        // given
        TodoList todoList = createTodoList();

        // stub
        when(todoListRepository.findByDueDate(todoList.getMemberId(), todoList.getDueDate())).thenReturn(todoList);

        // when
        TodoListResponseDto.Info result = todoListService.findByDueDate(todoList.getMemberId(), todoList.getDueDate());

        // then
        assertThat(todoList.getId()).isEqualTo(result.getId());
        assertThat(todoList.getDueDate()).isEqualTo(result.getDueDate());
    }

    @Test
    @DisplayName("TodoList 년월로 조회")
    public void findAllByYearAndMonth(){
        // given
        Long memberId = 1L;
        String yearMonth = "2023-06";
        List<TodoListResponseDto.Info> infoList = createTodoLists();

        // stub
        when(todoListRepository.findAllByYearMonth(any(Long.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(infoList);

        // when
        List<TodoListResponseDto.Info> result = todoListService.findAllByYearMonth(memberId, yearMonth);

        // then
        assertThat(result.size()).isEqualTo(infoList.size());
    }

    //region TodoList의 Todo 개수 수정 테스트
    @Test
    @DisplayName("TodoList의 Todo 개수 수정 - Todo가 생성 됐을 때, DueDate 날짜의 TodoList 데이터가 없을 경우")
    public void updateTodoCount_NotFoundTodoList(){
        // given
        Todo todo = createTodo();
        boolean isCreated = true;
        TodoList todoList = createTodoList();

        // stub
        when(todoListRepository.findByDueDate(any(Long.class), any(LocalDate.class))).thenReturn(null);
        when(todoListRepository.save(any(TodoList.class))).thenReturn(todoList);

        // when
        todoListService.updateTodoCount(todo, isCreated);

        // then
        verify(todoListRepository, times(1)).save(any(TodoList.class));
    }

    @Test
    @DisplayName("TodoList의 Todo 개수 수정 - Todo가 생성 됐을 때, DueDate 날짜의 TodoList 데이터가 존재할 경우")
    public void updateTodoCount_ExistsTodoList(){
        // given
        Todo todo = createTodo();
        boolean isCreated = true;

        // 3개의 Todo가 존재하고 전부 완료한 상태
        TodoList todoList = new TodoList(1L, 3L, 3L, 3, LocalDate.now());

        // stub
        when(todoListRepository.findByDueDate(any(Long.class), any(LocalDate.class))).thenReturn(todoList);

        // when
        todoListService.updateTodoCount(todo, isCreated);

        // then
        assertThat(todoList.getCount()).isEqualTo(4L); // Todo가 추가되었으니 4
        assertThat(todoList.getCompletedCount()).isEqualTo(3L);
        assertThat(todoList.getStep()).isEqualTo(2); // 4개 중에 3개가 완료됐으니 75%로 2단계
    }

    @Test
    @DisplayName("TodoList의 Todo 개수 수정 - Todo가 삭제 됐을 때, DueDate 날짜의 Todo 데이터가 없을 경우")
    public void updateTodoCount_TodoCountZero(){
        // given
        Todo todo = createTodo();
        boolean isCreated = false; // 삭제할 것이기 때문에 false

        // 1개의 Todo가 존재
        TodoList todoList = new TodoList(1L, 1L, 0L, 0, LocalDate.now());

        // stub
        when(todoListRepository.findByDueDate(any(Long.class), any(LocalDate.class))).thenReturn(todoList);

        // when
        todoListService.updateTodoCount(todo, isCreated);

        // then
        // Todo의 개수가 0개가 되었기 때문에 TodoList를 삭제
        verify(todoListRepository, times(1)).delete(any(TodoList.class));
    }

    @Test
    @DisplayName("TodoList의 Todo 개수 수정 - Todo가 삭제 됐을 때, DueDate 날짜의 Todo 데이터가 존재할 경우")
    public void updateTodoCount_TodoExists(){
        // given
        Todo todo = createTodo();
        boolean isCreated = false; // 삭제할 것이기 때문에 false

        // 2개의 Todo가 존재
        TodoList todoList = new TodoList(1L, 2L, 0L, 0, LocalDate.now());

        // stub
        when(todoListRepository.findByDueDate(any(Long.class), any(LocalDate.class))).thenReturn(todoList);

        // when
        todoListService.updateTodoCount(todo, isCreated);

        // then
        assertThat(todoList.getCount()).isEqualTo(1L); // Todo 1개가 삭제되었으니
    }
    //endregion

    //region TodoList의 Todo 완료 개수 수정
    @Test
    @DisplayName("TodoList의 Todo 완료 개수 수정 - Todo가 미완료에서 완료로")
    public void updateTodoCompleted_미완료에서완료로(){
        // given
        Todo todo = createTodo();
        todo.updateCompleted(); // isCompleted - true로 변경

        // 3개의 Todo가 존재하고 전부 완료한 상태
        TodoList todoList = new TodoList(1L, 3L, 2L, 3, LocalDate.now());

        // stub
        when(todoListRepository.findByDueDate(any(Long.class), any(LocalDate.class))).thenReturn(todoList);

        // when
        todoListService.updateTodoCompleted(todo);

        // then
        assertThat(todoList.getCompletedCount()).isEqualTo(3L);
        assertThat(todoList.getStep()).isEqualTo(3); // 3개 중에 3개가 완료됐으니 100%로 3단계
    }

    @Test
    @DisplayName("TodoList의 Todo 완료 개수 수정 - Todo가 완료에서 미완료로")
    public void updateTodoCompleted_완료에서미완료로(){
        // given
        Todo todo = createTodo();

        // 3개의 Todo가 존재하고 전부 완료한 상태
        TodoList todoList = new TodoList(1L, 3L, 3L, 3, LocalDate.now());

        // stub
        when(todoListRepository.findByDueDate(any(Long.class), any(LocalDate.class))).thenReturn(todoList);

        // when
        todoListService.updateTodoCompleted(todo);

        // then
        assertThat(todoList.getCompletedCount()).isEqualTo(2L);
        assertThat(todoList.getStep()).isEqualTo(1); // 3개 중에 2개가 완료됐으니 66%로 1단계
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

    private TodoList createTodoList(){
        return TodoList.builder()
                .memberId(1L)
                .count(1L)
                .completedCount(0L)
                .step(0)
                .dueDate(LocalDate.now())
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
