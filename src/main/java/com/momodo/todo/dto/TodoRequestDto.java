package com.momodo.todo.dto;

import com.momodo.todo.Todo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class TodoRequestDto {

    @Getter
    @NoArgsConstructor
    public static class Create{

        @NotBlank(message = "제목은 필수 입력 값입니다.")
        private String title;

        @NotBlank(message = "이모지는 필수 입력 값입니다.")
        private String emoji;

        private LocalDate dueDate;

        private String repeatDays;

        @Builder
        public Create(String title, String emoji, LocalDate dueDate, String repeatDays) {
            this.title = title;
            this.emoji = emoji;
            this.dueDate = dueDate;
            this.repeatDays = repeatDays;
        }

        public Todo toEntity(){
            return Todo.builder()
                    .title(title)
                    .emoji(emoji)
                    .dueDate(dueDate)
                    .isCompleted(false) // 새로 생성한 Todo이기 때문에 false
                    .repeatDays(repeatDays)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Edit{

        @NotBlank(message = "제목은 필수 입력 값입니다.")
        private String title;

        @NotBlank(message = "이모지는 필수 입력 값입니다.")
        private String emoji;

        private String repeatDays;

        @Builder
        public Edit(String title, String emoji, String repeatDays) {
            this.title = title;
            this.emoji = emoji;
            this.repeatDays = repeatDays;
        }
    }
}
