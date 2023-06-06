package com.momodo.todo.repository;

import com.momodo.todo.dto.TodoResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.momodo.todo.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TodoResponseDto.Info> findAllByDueDate(LocalDate dueDate) {

        return queryFactory
                .select(Projections.constructor(TodoResponseDto.Info.class,
                        todo.id,
                        todo.title,
                        todo.emoji,
                        todo.dueDate,
                        todo.isCompleted,
                        todo.repeatDays
                ))
                .from(todo)
                .where(todo.dueDate.eq(dueDate))
                .fetch();
    }
}
