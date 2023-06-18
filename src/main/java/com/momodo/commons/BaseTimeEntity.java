package com.momodo.commons;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

//@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@MappedSuperclass
public class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime lastModifiedDate = LocalDateTime.now();


}
