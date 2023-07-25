package com.momodo.emojihistory.repository;

import com.momodo.emojihistory.EmojiHistory;
import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.momodo.emojihistory.QEmojiHistory.emojiHistory;

@Repository
@RequiredArgsConstructor
public class EmojiHistoryRepositoryImpl implements EmojiHistoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public String findUsedEmojis(String memberId) {
        String emojis = queryFactory
                .select(emojiHistory.emojis)
                .from(emojiHistory)
                .where(emojiHistory.memberId.eq(memberId))
                .fetchOne();

        return emojis;
    }
}
