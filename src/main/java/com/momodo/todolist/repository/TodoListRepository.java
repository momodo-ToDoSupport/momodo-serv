package com.momodo.todolist.repository;

import com.momodo.todolist.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListRepository extends JpaRepository<TodoList, Long>, TodoListRepositoryCustom{
}
