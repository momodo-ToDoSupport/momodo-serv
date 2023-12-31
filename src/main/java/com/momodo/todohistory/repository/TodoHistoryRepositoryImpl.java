package com.momodo.todohistory.repository;

import com.momodo.todohistory.domain.TodoHistory;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.momodo.todohistory.domain.QTodoHistory.todoHistory;

@Repository
@RequiredArgsConstructor
public class TodoHistoryRepositoryImpl implements TodoHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createAll(List<TodoHistory> todoHistories) {
        String sql = "INSERT INTO todoHistory (member_id, count, completed_count, step, due_date) " +
                "VALUES(?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        TodoHistory todoHistory = todoHistories.get(i);
                        ps.setString(1, todoHistory.getMemberId());
                        ps.setLong(2, todoHistory.getCount());
                        ps.setLong(3, todoHistory.getCompletedCount());
                        ps.setInt(4, todoHistory.getStep());
                        ps.setString(5, todoHistory.getDueDate().toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1000;
                    }
                }
        );
    }

    @Override
    public Optional<TodoHistoryResponseDto.Info> findByDueDate(String memberId, LocalDate dueDate) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(TodoHistoryResponseDto.Info.class,
                        todoHistory.id,
                        todoHistory.count,
                        todoHistory.completedCount,
                        todoHistory.step,
                        todoHistory.dueDate
                ))
                .from(todoHistory)
                .where(todoHistory.memberId.eq(memberId)
                        .and(todoHistory.dueDate.eq(dueDate)))
                .fetchOne());
    }

    @Override
    public List<TodoHistoryResponseDto.Info> findAllByYearMonth(String memberId, LocalDate from, LocalDate to) {
        return queryFactory
                .select(Projections.constructor(TodoHistoryResponseDto.Info.class,
                        todoHistory.id,
                        todoHistory.count,
                        todoHistory.completedCount,
                        todoHistory.step,
                        todoHistory.dueDate
                ))
                .from(todoHistory)
                .where(todoHistory.memberId.eq(memberId)
                        .and(todoHistory.dueDate.between(from, to)))
                .fetch();
    }

    @Override
    public Map<String, Long> countBySecondStepAchievement(LocalDate from, LocalDate to) {
        List<Tuple> result = queryFactory
                .select(todoHistory.memberId, todoHistory.count())
                .from(todoHistory)
                .where(todoHistory.dueDate.between(from, to)
                        .and(todoHistory.step.goe(2)))
                .groupBy(todoHistory.memberId)
                .fetch();

        return result.stream()
                .collect(Collectors.toMap(
                        t -> t.get(0, String.class),
                        t -> t.get(1, Long.class)));
    }
}
