package com.momodo.todolist;

import com.momodo.TestConfig;
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
        TodoListResponseDto.Info todoListInfo = todoListRepository.findByDueDate(createdTodoList.getMemberId(), createdTodoList.getDueDate());

        // then
        assertThat(todoListInfo.getId()).isEqualTo(createdTodoList.getId());
        assertThat(todoListInfo.getDueDate()).isEqualTo(createdTodoList.getDueDate());
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
