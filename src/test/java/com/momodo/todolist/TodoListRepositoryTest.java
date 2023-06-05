package com.momodo.todolist;

import com.momodo.TestConfig;
import com.momodo.todo.Todo;
import com.momodo.todolist.dto.TodoListResponseDto;
import com.momodo.todolist.repository.TodoListRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class TodoListRepositoryTest {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private TodoListRepository todoListRepository;

    @Test
    @DisplayName("TodoList 등록")
    public void create(){
        // given
        TodoList todoList = createTodoList();

        // when
        TodoList createdTodoList = todoListRepository.save(todoList);

        // then
        assertThat(todoList.getId()).isEqualTo(createdTodoList.getId());
    }

    @Test
    @DisplayName("TodoList DueDate로 조회")
    public void findByDueDate(){
        // given
        TodoList createdTodoList = todoListRepository.save(createTodoList());

        // when
        TodoList foundTodoList = todoListRepository.findByDueDate(createdTodoList.getMemberId(), createdTodoList.getDueDate());

        // then
        assertThat(foundTodoList.getId()).isEqualTo(createdTodoList.getId());
        assertThat(foundTodoList.getDueDate()).isEqualTo(createdTodoList.getDueDate());
    }

    @Test
    @DisplayName("TodoList 년월로 조회")
    public void findAllByYearMonth(){
        // given
        TodoList createdTodoList = todoListRepository.save(createTodoList());
        LocalDate date = LocalDate.now();
        LocalDate firstDate = date.withDayOfMonth(1);
        LocalDate lastDate = date.withDayOfMonth(date.lengthOfMonth());

        // when
        List<TodoListResponseDto.Info> result = todoListRepository.findAllByYearMonth(createdTodoList.getMemberId(), firstDate, lastDate);

        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getDueDate().getMonthValue()).isEqualTo(date.getMonthValue());
    }

    @Test
    @DisplayName("TodoList의 Todo 개수 수정")
    public void updateTodoCount(){
        // given
        TodoList createdTodoList = todoListRepository.save(createTodoList());
        Long editCount = 2L;
        Long editCompletedCount = 1L;
        Integer editStep = 1;

        // when
        createdTodoList.update(editCount, editCompletedCount, editStep);

        // then
        TodoList foundTodoList = todoListRepository.findById(createdTodoList.getId()).get();
        assertThat(foundTodoList.getCount()).isEqualTo(editCount);
        assertThat(foundTodoList.getCompletedCount()).isEqualTo(editCompletedCount);
        assertThat(foundTodoList.getStep()).isEqualTo(editStep);
    }

    @Test
    @DisplayName("TodoList 삭제")
    public void delete(){
        // given
        TodoList createdTodoList = todoListRepository.save(createTodoList());
        Long id = createdTodoList.getId();

        // when
        todoListRepository.delete(createdTodoList);

        // then
        boolean isFound= todoListRepository.existsById(id);
        assertThat(isFound).isFalse(); // 데이터가 존재하지 않음
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
}
