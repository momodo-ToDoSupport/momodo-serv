package com.momodo.todohistory.repository;

import com.momodo.todohistory.TodoHistory;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface TodoHistoryRepositoryCustom {

    TodoHistory findByDueDate(Long memberId, LocalDate dueDate);

    List<TodoHistoryResponseDto.Info> findAllByYearMonth(Long memberId, LocalDate from, LocalDate to);
}
