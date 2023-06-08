package com.momodo.todolist;

import com.momodo.todolist.dto.TodoListResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoListController.class)
public class TodoListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoListService todoListService;

    @Test
    @DisplayName("TodoList DueDate로 조회")
    public void findByDueDate() throws Exception{
        // given
        Long memberId = 1L;
        LocalDate dueDate = LocalDate.now();
        String url = "/todolists/dueDate";

        TodoListResponseDto.Info info = createTodoListInfo(dueDate);
        doReturn(info).when(todoListService).findByDueDate(memberId, dueDate);

        // when & then
        mockMvc.perform(get(url)
                        .param("memberId", memberId.toString())
                        .param("dueDate", dueDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(info.getId()))
                        .andExpect(jsonPath("$.dueDate").value(info.getDueDate().toString()));
    }

    @Test
    @DisplayName("TodoList 년월로 조회")
    public void findAllByYearMonth() throws Exception{
        // given
        Long memberId = 1L;
        String yearMonth = "2023-06";
        String url = "/todolists/yearMonth";

        List<TodoListResponseDto.Info> infoList = Collections.emptyList();
        doReturn(infoList).when(todoListService).findAllByYearMonth(memberId, yearMonth);

        // when & then
        mockMvc.perform(get(url)
                        .param("memberId", memberId.toString())
                        .param("yearMonth", yearMonth)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    private TodoListResponseDto.Info createTodoListInfo(LocalDate date){
        return TodoListResponseDto.Info.builder()
                .id(1L)
                .count(1L)
                .completedCount(0L)
                .step(0)
                .dueDate(date)
                .build();
    }
}
