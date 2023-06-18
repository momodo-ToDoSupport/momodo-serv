package com.momodo.todohistory.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class TodoHistoryResponseDto {

    @Getter
    @NoArgsConstructor
    public static class Info{

        private Long id;
        private Long count;
        private Long completedCount;
        private Integer step;
        private LocalDate dueDate;

        @Builder
        public Info(Long id, Long count, Long completedCount, Integer step, LocalDate dueDate) {
            this.id = id;
            this.count = count;
            this.completedCount = completedCount;
            this.step = step;
            this.dueDate = dueDate;
        }
    }
}
