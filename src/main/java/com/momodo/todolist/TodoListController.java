package com.momodo.todolist;

import com.momodo.todolist.dto.TodoListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Find By DueDate", description = "마감 날짜로 TodoList 찾기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("/todolists/dueDate")
    public TodoListResponseDto.Info findByDueDate(@RequestParam Long memberId, @RequestParam LocalDate dueDate){

        return todoListService.findByDueDate(memberId, dueDate);
    }


    @Operation(summary = "FindAll By YearMonth", description = "년월에 해당하는 TodoLists 찾기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("/todolists/yearMonth")
    public List<TodoListResponseDto.Info> findAllByYearMonth(@RequestParam Long memberId, @RequestParam String yearMonth){

        return todoListService.findAllByYearMonth(memberId, yearMonth);
    }
}
