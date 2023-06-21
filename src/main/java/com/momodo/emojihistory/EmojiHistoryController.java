package com.momodo.emojihistory;

import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/emoji-histories")
public class EmojiHistoryController {

    private final EmojiHistoryService emojiHistoryService;

    @Operation(summary = "FindAll By Member", description = "사용자가 사용한 최근 이모지들 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('MEMBER')")
    public List<EmojiHistoryResponseDto.Info> findAllByMember(@AuthenticationPrincipal User user){
        String memberId = user.getUsername();
        return emojiHistoryService.findAllByMember(memberId);
    }
}
