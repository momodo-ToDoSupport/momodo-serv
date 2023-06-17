package com.momodo.emojihistory.repository;

import com.momodo.emojihistory.EmojiHistory;
import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.momodo.emojihistory.QEmojiHistory.emojiHistory;

@Repository
@RequiredArgsConstructor
public class EmojiHistoryRepositoryImpl implements EmojiHistoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public EmojiHistory findByEmoji(Long memberId, String emoji) {
        return queryFactory
                .selectFrom(emojiHistory)
                .where(emojiHistory.memberId.eq(memberId)
                        .and(emojiHistory.emoji.eq(emoji)))
                .fetchOne();
    }
}
