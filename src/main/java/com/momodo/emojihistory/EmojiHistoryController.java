package com.momodo.emojihistory;

import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="이모지 사용 기록", description = "사용자가 최근 사용한 이모지에 관한 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/emoji-histories")
public class EmojiHistoryController {

    private final EmojiHistoryService emojiHistoryService;

    @Operation(summary = "Find UsedEmojis", description = "사용자가 사용한 최근 이모지들 정보 조회")
    @GetMapping
    public ResponseEntity<EmojiHistoryResponseDto.UsedEmojis> findUsedEmojis(@AuthenticationPrincipal User user){
        String memberId = user.getUsername();
        return ResponseEntity.ok(emojiHistoryService.findUsedEmojis(memberId));
    }
}
