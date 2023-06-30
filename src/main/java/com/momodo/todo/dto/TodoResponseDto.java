package com.momodo.todo.dto;

import com.momodo.todo.Todo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class TodoResponseDto {

    @Getter
    @Builder
    public static class Info{

        private Long id;
        private String title;
        private String emoji;
        private LocalDate dueDate;
        private boolean isCompleted;

        private String repeatDays;

        @Builder
        public Info(Long id, String title, String emoji, LocalDate dueDate, boolean isCompleted, String repeatDays) {
            this.id = id;
            this.title = title;
            this.emoji = emoji;
            this.dueDate = dueDate;
            this.isCompleted = isCompleted;
            this.repeatDays = repeatDays;
        }
    }
}
