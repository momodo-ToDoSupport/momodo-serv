package com.momodo.todohistory.repository;

import com.momodo.todohistory.TodoHistory;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.momodo.todohistory.QTodoHistory.todoHistory;

@Repository
@RequiredArgsConstructor
public class TodoHistoryRepositoryImpl implements TodoHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public TodoHistory findByDueDate(String memberId, LocalDate dueDate) {
        return queryFactory
                .selectFrom(todoHistory)
                .where(todoHistory.memberId.eq(memberId)
                        .and(todoHistory.dueDate.eq(dueDate)))
                .fetchOne();
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
}
