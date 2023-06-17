package com.momodo.emojihistory.repository;

import com.momodo.emojihistory.EmojiHistory;
import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.momodo.emojihistory.QEmojiHistory.emojiHistory;

@Repository
@RequiredArgsConstructor
public class EmojiHistoryRepositoryImpl implements EmojiHistoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public EmojiHistory findByEmoji(Long memberId, String emoji) {
        return queryFactory
                .select(Projections.fields(EmojiHistory.class,
                        emojiHistory.id,
                        Expressions.asNumber(memberId),
                        Expressions.asString(emoji)
                ))
                .where(emojiHistory.memberId.eq(memberId)
                        .and(emojiHistory.emoji.eq(emoji)))
                .fetchOne();
    }

    @Override
    public List<EmojiHistoryResponseDto.Info> findAllByMember(Long memberId) {
        return queryFactory
                .select(Projections.constructor(EmojiHistoryResponseDto.Info.class,
                        emojiHistory.id,
                        emojiHistory.emoji
                ))
                .from(emojiHistory)
                .where(emojiHistory.memberId.eq(memberId))
                .fetch();
    }

    @Override
    public void deleteOldest(Long memberId) {
        long id = queryFactory
                .select(emojiHistory.id)
                .from(emojiHistory)
                .where(emojiHistory.memberId.eq(memberId))
                .orderBy(emojiHistory.createdDate.asc())
                .fetchFirst();

        queryFactory
                .delete(emojiHistory)
                .where(emojiHistory.id.eq(id))
                .execute();
    }
}
