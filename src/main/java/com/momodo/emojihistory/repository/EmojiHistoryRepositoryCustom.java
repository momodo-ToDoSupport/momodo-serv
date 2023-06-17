package com.momodo.emojihistory.repository;

import com.momodo.emojihistory.EmojiHistory;
import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;

public interface EmojiHistoryRepositoryCustom {

    EmojiHistory findByEmoji(Long memberId, String emoji);
}
