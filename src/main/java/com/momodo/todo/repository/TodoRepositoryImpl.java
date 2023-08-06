package com.momodo.todo.repository;

import com.momodo.todo.Todo;
import com.momodo.todo.dto.TodoResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static com.momodo.todo.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom{

    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    @Override
    public void saveTodos(List<Todo> todos) {
        String sql = "INSERT INTO todo (member_id, title, emoji, due_date, is_completed, created_date, last_modified_date) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Todo todo = todos.get(i);

                        ps.setString(1, todo.getMemberId());
                        ps.setString(2, todo.getTitle());
                        ps.setString(3, todo.getEmoji());
                        ps.setString(4, todo.getDueDate().toString());
                        ps.setBoolean(5, todo.isCompleted());
                        ps.setString(6, todo.getCreatedDate().toString());
                        ps.setString(7, todo.getLastModifiedDate().toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return todos.size();
                    }
                }
        );
    }

    @Override
    public List<Todo> findAllByDueDate(LocalDate dueDate) {
        return queryFactory
                .selectFrom(todo)
                .where(todo.dueDate.eq(dueDate))
                .fetch();
    }

    @Override
    public List<TodoResponseDto.Info> findByMemberAndDueDate(String memberId, LocalDate dueDate) {
        return queryFactory
                .select(Projections.constructor(TodoResponseDto.Info.class,
                        todo.id,
                        todo.title,
                        todo.emoji,
                        todo.dueDate,
                        todo.isCompleted
                ))
                .from(todo)
                .where(todo.memberId.eq(memberId)
                        .and(todo.dueDate.eq(dueDate)))
                .fetch();
    }

    @Override
    public List<TodoResponseDto.Info> findNotCompleteInYearMonth(String memberId, LocalDate from, LocalDate to) {
        return queryFactory
                .select(Projections.constructor(TodoResponseDto.Info.class,
                        todo.id,
                        todo.title,
                        todo.emoji,
                        todo.dueDate,
                        todo.isCompleted
                ))
                .from(todo)
                .where(todo.memberId.eq(memberId)
                        .and(todo.dueDate.between(from, to))
                        .and(todo.isCompleted.isFalse()))
                .fetch();
    }
}
