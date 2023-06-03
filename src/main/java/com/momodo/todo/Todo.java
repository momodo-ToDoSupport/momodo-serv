package com.momodo.todo;

import com.momodo.commons.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @Column(name = "member_id", nullable=false)
    private Long memberId;

    @Column(nullable = false)
    private String title;

    @Column(name = "is_completed", nullable=false)
    private boolean isCompleted;

    @Column(name = "repeat_days", nullable=false)
    private String repeatDays;

    @Column(nullable = false)
    private String emoji;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
}
