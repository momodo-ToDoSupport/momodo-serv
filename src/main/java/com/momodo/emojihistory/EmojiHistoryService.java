package com.momodo.emojihistory;

import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;
import com.momodo.emojihistory.repository.EmojiHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmojiHistoryService {

    private final EmojiHistoryRepository emojiHistoryRepository;

    private final int SAVE_MAX_SIZE = 10;

    @Transactional
    public void create(Long memberId, String emoji){
        // 이미 저장되어 있는 이모지라면 날짜 최신화
        if(emojiHistoryRepository.existsByMemberIdAndEmoji(memberId, emoji)){
            EmojiHistory foundEmojiHistory = emojiHistoryRepository.findByEmoji(memberId, emoji);
            foundEmojiHistory.setCreatedDate(LocalDateTime.now());
            return;
        }

        long savedCount = emojiHistoryRepository.countByMemberId(memberId);

        // 저장 가능한 이모지 개수와 같다면, 사용한지 가장 오래된 이모지 제거
        if(savedCount == SAVE_MAX_SIZE){
            emojiHistoryRepository.deleteOldest(memberId);
        }

        EmojiHistory emojiHistory = EmojiHistory.builder()
                .memberId(memberId)
                .emoji(emoji)
                .build();

        emojiHistoryRepository.save(emojiHistory);
    }

    @Transactional(readOnly = true)
    public List<EmojiHistoryResponseDto.Info> findAllByMember(Long memberId){
        return emojiHistoryRepository.findAllByMember(memberId);
    }
}
