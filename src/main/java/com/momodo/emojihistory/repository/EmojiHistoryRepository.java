package com.momodo.emojihistory.repository;

import com.momodo.emojihistory.EmojiHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmojiHistoryRepository extends JpaRepository<EmojiHistory, Long>, EmojiHistoryRepositoryCustom {
}
