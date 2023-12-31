package com.momodo.todohistory;

import com.momodo.jwt.dto.DataResponse;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name="TodoHistory", description = "일별 Todo 달성률에 관한 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todo-histories")
public class TodoHistoryController {

    private final TodoHistoryService todoHistoryService;

    @Operation(summary = "마감 날짜로 TodoHistory 정보 가져오기")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping("/dueDate")
    public DataResponse<TodoHistoryResponseDto.Info> findByDueDate(@RequestParam LocalDate dueDate
            , @AuthenticationPrincipal User user){

        String memberId = user.getUsername();
        TodoHistoryResponseDto.Info info = todoHistoryService.findByDueDate(memberId, dueDate);

        return DataResponse.of(info);
    }

    @Operation(summary = "년월에 해당하는 TodoHistory들 정보 가져오기")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping("/yearMonth")
    public DataResponse<List<TodoHistoryResponseDto.Info>> findAllByYearMonth(@RequestParam String yearMonth
            , @AuthenticationPrincipal User user){

        String memberId = user.getUsername();
        List<TodoHistoryResponseDto.Info> infos = todoHistoryService.findAllByYearMonth(memberId, yearMonth);

        return DataResponse.of(infos);
    }
}
