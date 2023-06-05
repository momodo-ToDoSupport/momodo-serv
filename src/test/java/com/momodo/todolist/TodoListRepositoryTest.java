package com.momodo.todolist;

import com.momodo.TestConfig;
import com.momodo.todolist.repository.TodoListRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

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
