package com.momodo.emojihistory;

import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;
import com.momodo.emojihistory.repository.EmojiHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmojiHistoryService {

    private final EmojiHistoryRepository emojiHistoryRepository;

    private final int SAVE_MAX_SIZE = 10;

    @Transactional
    public void addEmoji (String memberId, String addEmoji){

        EmojiHistory emojiHistory = emojiHistoryRepository.findByMemberId(memberId)
                .orElse(null);

        if(emojiHistory == null){
            emojiHistory = EmojiHistory.builder()
                    .memberId(memberId)
                    .emojis(addEmoji)
                    .build();

            emojiHistoryRepository.save(emojiHistory);
        }else{
            String updateEmojis = updateEmojis(emojiHistory.getEmojis(), addEmoji);

            emojiHistory.update(updateEmojis);
        }
    }

    public EmojiHistoryResponseDto.UsedEmojis findUsedEmojis(String memberId){
        List<String> usedEmojis = splitEmojis(emojiHistoryRepository.findUsedEmojis(memberId));
        return new EmojiHistoryResponseDto.UsedEmojis(usedEmojis);
    }

    private List<String> splitEmojis(String emojis){
        return Arrays.stream(emojis.split(",")).toList();
    }

    private String updateEmojis(String emojis, String addEmoji){
        List<String> emojiList = new ArrayList<>(splitEmojis(emojis));

        if(emojiList.contains(addEmoji)){
            emojiList.remove(addEmoji);
            emojiList.add(0, addEmoji);
        }else{
            if(emojiList.size() == SAVE_MAX_SIZE){
                // 가장 오래된 Emoji 제거
                emojiList.remove(emojiList.size() - 1);
            }

            // 첫 번째 index에 추가
            emojiList.add(0, addEmoji);
        }

        return emojiList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
