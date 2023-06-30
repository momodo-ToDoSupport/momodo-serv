package com.momodo.todohistory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class TodoHistoryResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Info{

        private Long id;
        private Long count;
        private Long completedCount;
        private Integer step;
        private LocalDate dueDate;
    }
}
