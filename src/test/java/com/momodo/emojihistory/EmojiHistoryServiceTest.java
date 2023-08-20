package com.momodo.emojihistory;

import com.momodo.emojihistory.repository.EmojiHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public void addEmoji(){
        // stub
        when(emojiHistoryRepository.findByMemberId(memberId)).thenReturn(Optional.empty());
        when(emojiHistoryRepository.save(any(EmojiHistory.class))).thenReturn(createEmojiHistory());

        // when
        emojiHistoryService.addEmoji(memberId, emoji);

        // then
        verify(emojiHistoryRepository, times(1)).save(any(EmojiHistory.class));
    }

    private EmojiHistory createEmojiHistory(){
        return EmojiHistory.builder()
                .memberId(memberId)
                .emojis(emoji)
                .build();
    }
}
