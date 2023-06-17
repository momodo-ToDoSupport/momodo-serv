package com.momodo.emojihistory;

import com.momodo.TestConfig;
import com.momodo.emojihistory.repository.EmojiHistoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class EmojiHistoryRepositoryTest {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private EmojiHistoryRepository emojiHistoryRepository;

    @Test
    @DisplayName("EmojiHistory 등록")
    public void create(){
        // given
        EmojiHistory emojiHistory = createEmojiHistory();

        // when
        EmojiHistory createdEmojiHistory = emojiHistoryRepository.save(emojiHistory);

        // then
        assertThat(emojiHistory.getId()).isEqualTo(createdEmojiHistory.getId());
    }

    private EmojiHistory createEmojiHistory(){
        return EmojiHistory.builder()
                .memberId(1L)
                .emoji("\uD83D\uDE01")
                .build();
    }
}
