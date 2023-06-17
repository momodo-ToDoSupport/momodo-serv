package com.momodo.emojihistory;

import com.momodo.emojihistory.repository.EmojiHistoryRepository;
import com.momodo.todohistory.TodoHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmojiHistoryServiceTest {

    @InjectMocks
    private EmojiHistoryService emojiHistoryService;

    @Mock
    private EmojiHistoryRepository emojiHistoryRepository;

    @Test
    @DisplayName("EmojiHistory 등록")
    public void create(){
        // given
        EmojiHistory emojiHistory = createEmojiHistory();

        // stub
        when(emojiHistoryRepository.save(any(EmojiHistory.class))).thenReturn(emojiHistory);

        // when
        emojiHistoryService.create(emojiHistory.getMemberId(), emojiHistory.getEmoji());

        // then
        verify(emojiHistoryRepository, times(1)).save(any(EmojiHistory.class));
    }

    private EmojiHistory createEmojiHistory(){
        return EmojiHistory.builder()
                .memberId(1L)
                .emoji("\uD83D\uDE01")
                .build();
    }
}
