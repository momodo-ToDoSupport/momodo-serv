package com.momodo.emojihistory;

import com.momodo.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EmojiHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emojiHistory_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String emoji;

    @Builder
    public EmojiHistory(Long memberId, String emoji) {
        this.memberId = memberId;
        this.emoji = emoji;
    }

    public void updateCreatedDate(LocalDateTime date){
        setCreatedDate(date);
    }
}
