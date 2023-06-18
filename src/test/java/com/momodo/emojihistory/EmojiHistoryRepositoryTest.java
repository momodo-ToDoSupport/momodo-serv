package com.momodo.emojihistory;

import com.momodo.TestConfig;
import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;
import com.momodo.emojihistory.repository.EmojiHistoryRepository;
import com.momodo.todohistory.TodoHistory;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class EmojiHistoryRepositoryTest {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private EmojiHistoryRepository emojiHistoryRepository;

    private Long memberId = 1L;
    private String emoji = "\uD83D\uDE01";

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

    @Test
    @DisplayName("EmojiHistory Emoji로 조회")
    public void findByEmoji(){
        // given
        EmojiHistory createdEmojiHistory = emojiHistoryRepository.save(createEmojiHistory());

        // when
        EmojiHistory foundEmojiHistory = emojiHistoryRepository.findByEmoji(memberId, emoji);

        // then
        assertThat(foundEmojiHistory.getId()).isEqualTo(createdEmojiHistory.getId());
    }

    @Test
    @DisplayName("EmojiHistories MemberId로 조회")
    public void findAllByMember(){
        // given
        List<EmojiHistory> createdEmojiHistories = emojiHistoryRepository.saveAll(createEmojiHistories());

        // when
        List<EmojiHistoryResponseDto.Info> foundEmojiHistories = emojiHistoryRepository.findAllByMember(memberId);

        // then
        assertThat(foundEmojiHistories.size()).isEqualTo(createdEmojiHistories.size());
    }
    
    @Test
    @DisplayName("EmojiHistory가 이미 저장돼있는지 체크")
    public void existsByMemberIdAndEmoji(){
        // given
        emojiHistoryRepository.save(createEmojiHistory());

        // when
        boolean isExist = emojiHistoryRepository.existsByMemberIdAndEmoji(memberId, emoji);

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("사용자의 EmojiHistory 개수 조회")
    public void countByMemberId(){
        // given
        List<EmojiHistory> createdEmojiHistories = emojiHistoryRepository.saveAll(createEmojiHistories());

        // when
        Long count = emojiHistoryRepository.countByMemberId(memberId);

        // then
        assertThat(count).isEqualTo(createdEmojiHistories.size());
    }

    @Test
    @DisplayName("사용자의 EmojiHistory 중 가장 오래된 데이터 제거")
    public void deleteOldest(){
        // given
        List<EmojiHistory> emojiHistories = createEmojiHistories();
        emojiHistories.get(1).setCreatedDate(LocalDateTime.now().minusHours(1));
        String emoji = emojiHistories.get(1).getEmoji();
        emojiHistoryRepository.saveAll(emojiHistories);

        // when
        emojiHistoryRepository.deleteOldest(memberId);
        List<EmojiHistoryResponseDto.Info> foundEmojiHistories = emojiHistoryRepository.findAllByMember(memberId);

        // then
        assertThat(foundEmojiHistories.size()).isEqualTo(emojiHistories.size() - 1);
        assertThat(foundEmojiHistories.get(0).getEmoji()).isEqualTo(emoji);
    }

    private EmojiHistory createEmojiHistory(){
        return EmojiHistory.builder()
                .memberId(1L)
                .emoji(emoji)
                .build();
    }

    private List<EmojiHistory> createEmojiHistories(){
        EmojiHistory e1 = new EmojiHistory(memberId, "\uD83D\uDE01");
        EmojiHistory e2 = new EmojiHistory(memberId, "\uD83D\uDE02");

        List<EmojiHistory> list = List.of(
                e1, e2
        );

        return list;
    }
}
