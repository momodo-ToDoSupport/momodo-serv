package com.momodo.emojihistory.repository;

import com.momodo.emojihistory.EmojiHistory;
import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;

import java.util.List;

public interface EmojiHistoryRepositoryCustom {

    EmojiHistory findByEmoji(Long memberId, String emoji);

    List<EmojiHistoryResponseDto.Info> findAllByMember(Long memberId);

    void deleteOldest(Long memberId);
}
