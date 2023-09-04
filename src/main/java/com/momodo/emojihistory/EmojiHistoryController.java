package com.momodo.emojihistory;

import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;
import com.momodo.jwt.dto.BasicResponse;
import com.momodo.jwt.dto.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="이모지 사용 기록", description = "사용자가 최근 사용한 이모지에 관한 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/emoji-histories")
public class EmojiHistoryController {

    private final EmojiHistoryService emojiHistoryService;

    @Operation(summary = "Find UsedEmojis", description = "사용자가 사용한 최근 이모지들 정보 조회")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping
    public DataResponse<EmojiHistoryResponseDto.UsedEmojis> findUsedEmojis(@AuthenticationPrincipal User user){
        String memberId = user.getUsername();
        EmojiHistoryResponseDto.UsedEmojis usedEmojis = emojiHistoryService.findUsedEmojis(memberId);

        return DataResponse.of(usedEmojis);
    }
}
