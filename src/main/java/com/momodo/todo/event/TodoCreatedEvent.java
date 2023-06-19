package com.momodo.todo.event;

public class TodoCreatedEvent {

    private Long memberId;
    private String emoji;

    public TodoCreatedEvent(Long memberId, String emoji){
        this.memberId = memberId;
        this.emoji = emoji;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getEmoji() {
        return emoji;
    }
}
