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
    private String memberId;

    @Column(nullable = false)
    private String emojis;

    @Builder
    public EmojiHistory(String memberId, String emojis) {
        this.memberId = memberId;
        this.emojis = emojis;
    }

    public void update(String emojis){
        this.emojis = emojis;
    }
}
