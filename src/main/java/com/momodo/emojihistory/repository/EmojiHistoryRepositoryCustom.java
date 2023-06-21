package com.momodo.emojihistory.repository;

import com.momodo.emojihistory.EmojiHistory;
import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;

import java.util.List;

public interface EmojiHistoryRepositoryCustom {

    EmojiHistory findByEmoji(String memberId, String emoji);

    List<EmojiHistoryResponseDto.Info> findAllByMember(String memberId);

    void deleteOldest(String memberId);
}
