package com.momodo.todo.repository;

import com.momodo.todo.Todo;
import com.momodo.todo.dto.TodoResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepositoryCustom {

    void saveTodos(List<Todo> todos);

    List<Todo> findAllByDueDate(LocalDate dueDate);

    List<TodoResponseDto.Info> findByMemberAndDueDate(String memberId, LocalDate dueDate);

    List<TodoResponseDto.Info> findNotCompleteInYearMonth(String memberId, LocalDate from, LocalDate to);
}
