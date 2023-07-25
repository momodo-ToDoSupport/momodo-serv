package com.momodo.emojihistory.repository;

import com.momodo.emojihistory.EmojiHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmojiHistoryRepository extends JpaRepository<EmojiHistory, Long>, EmojiHistoryRepositoryCustom {

    Optional<EmojiHistory> findByMemberId(String memberId);
}
