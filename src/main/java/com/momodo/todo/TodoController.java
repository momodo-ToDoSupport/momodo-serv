package com.momodo.todo;

import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.dto.TodoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "Create Todo", description = "Todo 생성하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "memberId", description = "사용자 아이디", example = "1"),
            @Parameter(name = "title", description = "Todo 제목", example = "운동하기"),
            @Parameter(name = "emoji", description = "이모지", example = "\uD83D\uDE01"),
            @Parameter(name = "dueDate", description = "Todo 마감 날짜", example = "2023-06-05"),
            @Parameter(name = "repeatDays", description = "Todo 반복 요일", example = "0: 일, 1: 월, 2:화, 3:수, 4:목, 5:금, 6:토\n"
            + "1. 화요일 반복 -> 2\n" + "2. 목, 금요일 반복 -> 4,5\n" + "3. 매일 반복 -> 0-6" )
    })
    @PostMapping("/todos")
    public void createTodo(@RequestBody @Valid TodoRequestDto.Create requestDto){

        todoService.createTodo(requestDto);
    }

    @Operation(summary = "Find By Id", description = "Id로 Todo 정보 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameter(name = "id", description = "Todo 아이디", example = "1")
    @GetMapping("/todos/{id}")
    public TodoResponseDto.Info findById(@PathVariable Long id){

        return todoService.findById(id);
    }

    @Operation(summary = "FindAll By DueDate", description = "DueDate로 날짜에 해당하는 Todo들의 정보 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameter(name = "dueDate", description = "Todo 마감 날짜", example = "2023-06-05")
    @GetMapping("/todos")
    public List<TodoResponseDto.Info> findAllByDueDate(@RequestParam LocalDate dueDate){

        return todoService.findAllByDueDate(dueDate);
    }
}
