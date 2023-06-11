package com.momodo.todohistory.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class TodoHistoryRequestDto {

    @Getter
    @NoArgsConstructor
    public static class Create{

        @NotNull(message = "사용자 아이디는 필수 입력 값입니다.")
        private Long memberId;

        @NotNull
        private Long count;

        @NotNull
        private Long completedCount;

        private LocalDate dueDate;

        @Builder
        public Create(Long memberId, Long count, Long completedCount, LocalDate dueDate) {
            this.memberId = memberId;
            this.count = count;
            this.completedCount = completedCount;
            this.dueDate = dueDate;
        }
    }
}
