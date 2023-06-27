package com.momodo.emojihistory.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmojiHistoryResponseDto {

    @Getter
    @Builder
    public static class Info {

        private Long id;
        private String emoji;

        public Info(Long id, String emoji) {
            this.id = id;
            this.emoji = emoji;
        }
    }
}
