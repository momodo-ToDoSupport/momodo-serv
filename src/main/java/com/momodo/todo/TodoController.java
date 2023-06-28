package com.momodo.todo;

import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.dto.TodoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name="Todo", description = "Todo에 관한 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "Create Todo", description = "Todo 생성하기")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @PostMapping
    public void createTodo(@RequestBody @Valid TodoRequestDto.Create requestDto
            , @AuthenticationPrincipal User user){

        String memberId = user.getUsername();
        todoService.createTodo(requestDto, memberId);
    }

    @Operation(summary = "Find By Id", description = "Id로 Todo 정보 가져오기")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping("/{id}")
    public TodoResponseDto.Info findById(@PathVariable Long id){

        return todoService.findById(id);
    }

    @Operation(summary = "Update Completed", description = "Todo 완료 여부 수정하기")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @PatchMapping("/{id}/updateCompleted")
    public void updateCompleted(@PathVariable Long id){

        todoService.updateCompleted(id);
    }

    @Operation(summary = "Update", description = "Todo 정보 수정")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @PatchMapping("/{id}")
    public void update(@PathVariable Long id,
                                             @RequestBody TodoRequestDto.Edit request){

        todoService.update(id, request);
    }

    @Operation(summary = "Delete", description = "Todo 삭제")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){

        todoService.deleteById(id);
    }
}
