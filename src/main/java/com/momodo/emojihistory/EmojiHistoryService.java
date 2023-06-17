package com.momodo.emojihistory;

import com.momodo.emojihistory.repository.EmojiHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmojiHistoryService {

    private final EmojiHistoryRepository emojiHistoryRepository;

    @Transactional
    public void create(Long memberId, String emoji){

        EmojiHistory emojiHistory = EmojiHistory.builder()
                .memberId(memberId)
                .emoji(emoji)
                .build();

        emojiHistoryRepository.save(emojiHistory);
    }
}
