package com.momodo.todo;

import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.dto.TodoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Create Todo", description = "Todo 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PostMapping("/todos")
    public void createTodo(@RequestBody @Valid TodoRequestDto.Create requestDto){

        todoService.createTodo(requestDto);
    }

    @Operation(summary = "FindById", description = "Todo Id로 Todo 정보 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 데이터")
    })
    @GetMapping("/todos/{id}")
    public TodoResponseDto.Info findById(@PathVariable Long id){

        return todoService.findById(id);
    }

    @Operation(summary = "FindAllByDueDate", description = "Todo DueDate로 Todo 정보 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("/todos")
    public List<TodoResponseDto.Info> findAllByDueDate(@RequestParam LocalDate dueDate){

        return todoService.findAllByDueDate(dueDate);
    }

    @Operation(summary = "Update Completed", description = "Todo 완료 여부 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PatchMapping("/todos/{id}/updateCompleted")
    public void updateCompleted(@PathVariable Long id,
                                                      @RequestBody TodoRequestDto.EditCompleted request){

        todoService.updateCompleted(id, request);
    }

    @Operation(summary = "Update", description = "Todo 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PatchMapping("/todos/{id}/update")
    public void update(@PathVariable Long id,
                                             @RequestBody TodoRequestDto.Edit request){

        todoService.update(id, request);
    }
}
