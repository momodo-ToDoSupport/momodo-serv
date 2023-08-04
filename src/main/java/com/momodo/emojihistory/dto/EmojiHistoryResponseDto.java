package com.momodo.emojihistory.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class EmojiHistoryResponseDto {

    @Getter
    @Builder
    public static class UsedEmojis {

        private List<String> emojis;

        public UsedEmojis(List<String> emojis) {
            this.emojis = emojis;
        }
    }
}
