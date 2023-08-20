package com.momodo.todohistory.domain;

import com.momodo.commons.BaseEntity;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class TodoHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todoHistory_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(nullable = false)
    private Long count;

    @Column(name = "completed_count", nullable = false)
    private Long completedCount;

    @Column(nullable = false)
    private Integer step;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Builder
    public TodoHistory(String memberId, Long count, Long completedCount, Integer step, LocalDate dueDate) {
        this.memberId = memberId;
        this.count = count;
        this.completedCount = completedCount;
        this.step = step;
        this.dueDate = dueDate;
    }

    public TodoHistoryResponseDto.Info toInfo(){

        return TodoHistoryResponseDto.Info.builder()
                .id(id)
                .count(count)
                .completedCount(completedCount)
                .step(step)
                .dueDate(dueDate)
                .build();
    }

    public void update(Long count, Long completedCount, Integer step){
        this.count = count;
        this.completedCount = completedCount;
        this.step = step;
    }
}
