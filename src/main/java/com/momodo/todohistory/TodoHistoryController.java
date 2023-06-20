package com.momodo.todohistory;

import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MEMBER')")
@RequestMapping("/api/v1/todo-histories")
public class TodoHistoryController {

    private final TodoHistoryService todoHistoryService;

    @Operation(summary = "Find By DueDate", description = "마감 날짜로 TodoHistory 정보 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "memberId", description = "사용자 아이디", example = "1"),
            @Parameter(name = "dueDate", description = "Todo 마감 날짜", example = "2023-06-05")
    })
    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping("/dueDate")
    public TodoHistoryResponseDto.Info findByDueDate(@RequestParam Long memberId, @RequestParam LocalDate dueDate){

        return todoHistoryService.findByDueDate(memberId, dueDate);
    }

    @Operation(summary = "FindAll By YearMonth", description = "년월에 해당하는 TodoHistory들 정보 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "memberId", description = "사용자 아이디", example = "1"),
            @Parameter(name = "yearMonth", description = "가져올 TodoHistory들의 년월", example = "2023-06 or 2023-09")
    })
    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping("/yearMonth")
    public List<TodoHistoryResponseDto.Info> findAllByYearMonth(@RequestParam Long memberId, @RequestParam String yearMonth){

        return todoHistoryService.findAllByYearMonth(memberId, yearMonth);
    }
}
