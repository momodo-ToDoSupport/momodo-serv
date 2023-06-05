package com.momodo.todolist.repository;

import com.momodo.todolist.dto.TodoListResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface TodoListRepositoryCustom {

    TodoListResponseDto.Info findByDueDate(Long memberId, LocalDate dueDate);

    List<TodoListResponseDto.Info> findAllByYearMonth(Long memberId, LocalDate from, LocalDate to);
}
