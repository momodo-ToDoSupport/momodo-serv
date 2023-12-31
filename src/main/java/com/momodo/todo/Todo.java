package com.momodo.todo;

import com.momodo.commons.BaseEntity;
import com.momodo.todo.dto.TodoResponseDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String emoji;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @Builder
    public Todo(String memberId, String title, String emoji, LocalDate dueDate, boolean isCompleted, String repeatDays) {
        this.memberId = memberId;
        this.title = title;
        this.emoji = emoji;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }

    public TodoResponseDto.Info toInfo(){
        return TodoResponseDto.Info.builder()
                .id(id)
                .title(title)
                .emoji(emoji)
                .dueDate(dueDate)
                .isCompleted(isCompleted)
                .build();
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void updateCompleted(){
        isCompleted = !isCompleted;
    }

    public void update(String title, String emoji){
        this.title = title;
        this.emoji = emoji;
    }
}
