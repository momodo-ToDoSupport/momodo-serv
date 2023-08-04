package com.momodo.emojihistory.repository;

import com.momodo.emojihistory.EmojiHistory;
import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;

import java.util.List;
import java.util.Optional;

public interface EmojiHistoryRepositoryCustom {

    String findUsedEmojis(String memberId);
}
