package com.momodo.todo.event;

public class TodoCreatedEvent {

    private String memberId;
    private String emoji;

    public TodoCreatedEvent(String memberId, String emoji){
        this.memberId = memberId;
        this.emoji = emoji;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getEmoji() {
        return emoji;
    }
}
