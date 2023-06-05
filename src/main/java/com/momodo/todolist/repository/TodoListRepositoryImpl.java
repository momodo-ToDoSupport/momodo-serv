package com.momodo.todolist.repository;

import com.momodo.todolist.dto.TodoListResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.momodo.todolist.QTodoList.todoList;

@Repository
@RequiredArgsConstructor
public class TodoListRepositoryImpl implements TodoListRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public TodoListResponseDto.Info findByDueDate(Long memberId, LocalDate dueDate) {
        return queryFactory
                .select(Projections.constructor(TodoListResponseDto.Info.class,
                        todoList.id,
                        todoList.count,
                        todoList.completedCount,
                        todoList.step,
                        todoList.dueDate
                ))
                .from(todoList)
                .where(todoList.memberId.eq(memberId)
                        .and(todoList.dueDate.eq(dueDate)))
                .fetchOne();
    }

    @Override
    public List<TodoListResponseDto.Info> findAllByYearMonth(Long memberId, LocalDate from, LocalDate to) {
        return queryFactory
                .select(Projections.constructor(TodoListResponseDto.Info.class,
                        todoList.id,
                        todoList.count,
                        todoList.completedCount,
                        todoList.step,
                        todoList.dueDate
                ))
                .from(todoList)
                .where(todoList.memberId.eq(memberId)
                        .and(todoList.dueDate.between(from, to)))
                .fetch();
    }
}
