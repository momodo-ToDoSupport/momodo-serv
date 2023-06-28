package com.momodo.todohistory;

import com.momodo.TestConfig;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import com.momodo.todohistory.repository.TodoHistoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class TodoHistoryRepositoryTest {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private TodoHistoryRepository todoHistoryRepository;

    private String memberId = "Test";

    @Test
    @DisplayName("TodoHistory DueDate로 조회")
    public void findByDueDate(){
        // given
        TodoHistory createdTodoHistory = todoHistoryRepository.save(createTodoHistory());

        // when
        TodoHistory foundTodoHistory = todoHistoryRepository.findByDueDate(createdTodoHistory.getMemberId(), createdTodoHistory.getDueDate());

        // then
        assertThat(foundTodoHistory.getId()).isEqualTo(createdTodoHistory.getId());
        assertThat(foundTodoHistory.getDueDate()).isEqualTo(createdTodoHistory.getDueDate());
    }

    @Test
    @DisplayName("TodoHistory 년월로 조회")
    public void findAllByYearMonth(){
        // given
        TodoHistory createdTodoHistory = todoHistoryRepository.save(createTodoHistory());
        LocalDate date = LocalDate.now();
        LocalDate firstDate = date.withDayOfMonth(1);
        LocalDate lastDate = date.withDayOfMonth(date.lengthOfMonth());

        // when
        List<TodoHistoryResponseDto.Info> result = todoHistoryRepository.findAllByYearMonth(createdTodoHistory.getMemberId(), firstDate, lastDate);

        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getDueDate().getMonthValue()).isEqualTo(date.getMonthValue());
    }

    @Test
    @DisplayName("TodoHistory의 Todo 개수 수정")
    public void updateTodoCount(){
        // given
        TodoHistory createdTodoHistory = todoHistoryRepository.save(createTodoHistory());
        Long editCount = 2L;
        Long editCompletedCount = 1L;
        Integer editStep = 1;

        // when
        createdTodoHistory.update(editCount, editCompletedCount, editStep);

        // then
        TodoHistory foundTodoHistory = todoHistoryRepository.findById(createdTodoHistory.getId()).get();
        assertThat(foundTodoHistory.getCount()).isEqualTo(editCount);
        assertThat(foundTodoHistory.getCompletedCount()).isEqualTo(editCompletedCount);
        assertThat(foundTodoHistory.getStep()).isEqualTo(editStep);
    }

    @Test
    @DisplayName("TodoHistory 삭제")
    public void delete(){
        // given
        TodoHistory createdTodoHistory = todoHistoryRepository.save(createTodoHistory());
        Long id = createdTodoHistory.getId();

        // when
        todoHistoryRepository.delete(createdTodoHistory);

        // then
        boolean isFound= todoHistoryRepository.existsById(id);
        assertThat(isFound).isFalse(); // 데이터가 존재하지 않음
    }

    private TodoHistory createTodoHistory(){
        return new TodoHistory(memberId, 1L, 0L, 0, LocalDate.now());
    }


    private List<TodoHistory> createTodoHistories(){
        TodoHistory t1 = new TodoHistory(memberId, 1L, 0L, 0, LocalDate.now());
        TodoHistory t2 = new TodoHistory(memberId, 2L, 0L, 0, LocalDate.now());
        TodoHistory t3 = new TodoHistory(memberId, 3L, 0L, 0, LocalDate.now());

        List<TodoHistory> todoHistories = List.of(
                t1, t2, t3
        );
        return todoHistories;
    }
}
