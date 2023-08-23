package com.momodo.todohistory.repository;

import com.momodo.todohistory.domain.TodoHistory;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TodoHistoryRepositoryCustom {

    void createAll(List<TodoHistory> todoHistories);

    TodoHistory findByDueDate(String memberId, LocalDate dueDate);

    List<TodoHistoryResponseDto.Info> findAllByYearMonth(String memberId, LocalDate from, LocalDate to);

    /**
     * 2단계를 달성한 TodoHistory 개수 계산
     *
     */
    Map<String, Long> countBySecondStepAchievement(LocalDate from, LocalDate to);
}
