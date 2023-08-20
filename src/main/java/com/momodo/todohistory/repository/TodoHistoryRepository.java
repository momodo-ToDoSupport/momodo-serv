package com.momodo.todohistory.repository;

import com.momodo.todohistory.domain.TodoHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoHistoryRepository extends JpaRepository<TodoHistory, Long>, TodoHistoryRepositoryCustom {
}
