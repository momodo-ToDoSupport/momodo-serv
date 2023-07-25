package com.momodo.todo.event;

import com.momodo.emojihistory.EmojiHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// 이벤트 리스너를 사용하여 핵심 로직과 부가 로직을 분리
@Component
@RequiredArgsConstructor
public class TodoCreateEventListener {

    private final EmojiHistoryService emojiHistoryService;

    @EventListener
    public void addEmoji(TodoCreatedEvent event){
        emojiHistoryService.addEmoji(event.getMemberId(), event.getEmoji());
    }
}
