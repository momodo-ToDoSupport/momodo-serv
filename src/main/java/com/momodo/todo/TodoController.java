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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> createTodo(@RequestBody @Valid TodoRequestDto.Create requestDto
            , @AuthenticationPrincipal User user){

        String memberId = user.getUsername();
        todoService.create(requestDto, memberId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Find By Id", description = "Id로 Todo 정보 조회")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto.Info> findById(@PathVariable Long id){

        return ResponseEntity.ok(todoService.findById(id));
    }

    @Operation(summary = "Find By DueDate", description = "날짜에 해당하는 Todo 정보 조회")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping("/date")
    public ResponseEntity<List<TodoResponseDto.Info>> findByMemberAndDueDate(@RequestParam LocalDate dueDate
            , @AuthenticationPrincipal User user){
        String memberId = user.getUsername();
        return ResponseEntity.ok(todoService.findByMemberAndDueDate(memberId, dueDate));
    }

    @Operation(summary = "Find Not-Complete In YearMonth", description = "년/월에 완료하지 못한 Todo 정보 조회")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping("/not-complete")
    public ResponseEntity<List<TodoResponseDto.Info>> findNotCompleteInYearMonth(@RequestParam String yearMonth
            , @AuthenticationPrincipal User user){
        String memberId = user.getUsername();
        return ResponseEntity.ok(todoService.findNotCompleteInYearMonth(memberId, yearMonth));
    }

    @Operation(summary = "Update Completed", description = "Todo 완료 여부 수정")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> updateCompleted(@PathVariable Long id){
        todoService.updateCompleted(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update", description = "Todo 정보 수정")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                             @RequestBody TodoRequestDto.Edit request){
        todoService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete", description = "Todo 삭제")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        todoService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
