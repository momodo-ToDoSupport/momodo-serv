package com.momodo.todo.dto;

import com.momodo.todo.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class TodoResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Info{

        private Long id;
        private String title;
        private String emoji;
        private LocalDate dueDate;
        private boolean isCompleted;
        private String repeatDays;
    }
}
