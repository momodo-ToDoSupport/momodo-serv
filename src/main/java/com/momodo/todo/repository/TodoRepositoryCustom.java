package com.momodo.todo.repository;

import com.momodo.todo.dto.TodoResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepositoryCustom {

    List<TodoResponseDto.Info> findAllByDueDate(LocalDate dueDate);
}
