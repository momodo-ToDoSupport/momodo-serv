package com.momodo.todolist;

import com.momodo.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class TodoList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todolist_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long count;

    @Column(name = "completed_count", nullable = false)
    private Long completedCount;

    @Column(nullable = false)
    private Integer step;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Builder
    public TodoList(Long memberId, Long count, Long completedCount, Integer step, LocalDate dueDate) {
        this.memberId = memberId;
        this.count = count;
        this.completedCount = completedCount;
        this.step = step;
        this.dueDate = dueDate;
    }
}
