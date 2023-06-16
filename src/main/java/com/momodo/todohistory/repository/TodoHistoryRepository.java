package com.momodo.todohistory.repository;

import com.momodo.todohistory.TodoHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TodoHistoryRepository extends JpaRepository<TodoHistory, Long>, TodoHistoryRepositoryCustom {
}
