package com.momodo.todolist;

import com.momodo.todolist.dto.TodoListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoListController {

    private final TodoListService todoListService;

    @Operation(summary = "Find By DueDate", description = "마감 날짜로 TodoList 정보 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "memberId", description = "사용자 아이디", example = "1"),
            @Parameter(name = "dueDate", description = "Todo 마감 날짜", example = "2023-06-05"),
    })
    @GetMapping("/todolists/dueDate")
    public TodoListResponseDto.Info findByDueDate(@RequestParam Long memberId, @RequestParam LocalDate dueDate){

        return todoListService.findByDueDate(memberId, dueDate);
    }

    @Operation(summary = "FindAll By YearMonth", description = "년월에 해당하는 TodoList들 정보 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "memberId", description = "사용자 아이디", example = "1"),
            @Parameter(name = "yearMonth", description = "가져올 TodoList들의 년월", example = "2023-06 or 2023-09"),
    })
    @GetMapping("/todolists/yearMonth")
    public List<TodoListResponseDto.Info> findAllByYearMonth(@RequestParam Long memberId, @RequestParam String yearMonth){

        return todoListService.findAllByYearMonth(memberId, yearMonth);
    }
}
