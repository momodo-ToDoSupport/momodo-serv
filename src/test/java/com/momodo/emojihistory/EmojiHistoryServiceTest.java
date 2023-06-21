package com.momodo.emojihistory;

import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;
import com.momodo.emojihistory.repository.EmojiHistoryRepository;
import com.momodo.todohistory.TodoHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class EmojiHistoryServiceTest {

    @InjectMocks
    private EmojiHistoryService emojiHistoryService;

    @Mock
    private EmojiHistoryRepository emojiHistoryRepository;

    private final int SAVE_MAX_SIZE = 10;
    private String memberId = "test";
    private String emoji = "\uD83D\uDE01";

    @Test
    @DisplayName("EmojiHistory 등록")
    public void create(){
        // stub
        when(emojiHistoryRepository.save(any(EmojiHistory.class))).thenReturn(createEmojiHistory());
        when(emojiHistoryRepository.existsByMemberIdAndEmoji(memberId, emoji)).thenReturn(false);
        when(emojiHistoryRepository.countByMemberId(memberId)).thenReturn(1L);

        // when
        emojiHistoryService.create(memberId, emoji);

        // then
        verify(emojiHistoryRepository, times(1)).save(any(EmojiHistory.class));
    }

    @Test
    @DisplayName("EmojiHistory 등록 - 이미 저장되어 있는 경우")
    public void create_alreadyExist(){
        // given
        EmojiHistory emojiHistory = createEmojiHistory();
        emojiHistory.setCreatedDate(LocalDateTime.now().minusHours(1)); // 테스트를 위해 -1시간

        // stub
        when(emojiHistoryRepository.existsByMemberIdAndEmoji(memberId, emoji)).thenReturn(true);
        when(emojiHistoryRepository.findByEmoji(memberId, emoji)).thenReturn(emojiHistory);

        // when
        emojiHistoryService.create(emojiHistory.getMemberId(), emojiHistory.getEmoji());

        // then
        assertThat(emojiHistory.getCreatedDate().getHour()).isEqualTo(LocalDateTime.now().getHour());
    }

    @Test
    @DisplayName("EmojiHistory 등록 - 저장되어 있는 이모지 개수가 MAX_SIZE")
    public void create_saveMaxSize(){
        // given
        EmojiHistory emojiHistory = createEmojiHistory();
        emojiHistory.setCreatedDate(LocalDateTime.now().minusHours(1)); // 테스트를 위해 -1시간

        // stub
        when(emojiHistoryRepository.existsByMemberIdAndEmoji(memberId, emoji)).thenReturn(false);
        when(emojiHistoryRepository.countByMemberId(memberId)).thenReturn((long)SAVE_MAX_SIZE);

        // when
        emojiHistoryService.create(emojiHistory.getMemberId(), emojiHistory.getEmoji());

        // then
        verify(emojiHistoryRepository, times(1)).deleteOldest(any(String.class));
        verify(emojiHistoryRepository, times(1)).save(any(EmojiHistory.class));
    }

    @Test
    @DisplayName("EmojiHistories MemberId로 조회")
    public void findAllByMember(){
        // given
        List<EmojiHistoryResponseDto.Info> infoList = createEmojiHistoryInfoList();

        // stub
        when(emojiHistoryRepository.findAllByMember(memberId)).thenReturn(infoList);

        // when
        List<EmojiHistoryResponseDto.Info> result = emojiHistoryService.findAllByMember(memberId);

        // then
        assertThat(result.size()).isEqualTo(infoList.size());
    }

    private EmojiHistory createEmojiHistory(){
        return EmojiHistory.builder()
                .memberId(memberId)
                .emoji(emoji)
                .build();
    }

    private List<EmojiHistoryResponseDto.Info> createEmojiHistoryInfoList(){
        EmojiHistoryResponseDto.Info info1 =
                new EmojiHistoryResponseDto.Info(1L, "\uD83D\uDE01");
        EmojiHistoryResponseDto.Info info2 =
                new EmojiHistoryResponseDto.Info(2L, "\uD83D\uDE02");

        List<EmojiHistoryResponseDto.Info> list = List.of(
                info1, info2
        );

        return list;
    }
}
