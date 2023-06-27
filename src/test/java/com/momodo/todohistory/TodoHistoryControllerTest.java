package com.momodo.todohistory;

import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoHistoryController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(roles = {"MEMBER"})
public class TodoHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoHistoryService todoHistoryService;

    private String memberId = "Test";

    @Test
    @DisplayName("TodoHistory DueDate로 조회")
    public void findByDueDate() throws Exception{
        // given
        LocalDate dueDate = LocalDate.now();
        String url = "/api/v1/todo-histories/dueDate";

        TodoHistoryResponseDto.Info info = createTodoHistoryInfo(dueDate);
        doReturn(info).when(todoHistoryService).findByDueDate(memberId, dueDate);

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
    @DisplayName("TodoHistory 년월로 조회")
    public void findAllByYearMonth() throws Exception{
        // given
        String yearMonth = "2023-06";
        String url = "/api/v1/todo-histories/yearMonth";

        List<TodoHistoryResponseDto.Info> infoList = Collections.emptyList();
        doReturn(infoList).when(todoHistoryService).findAllByYearMonth(memberId, yearMonth);

        // when & then
        mockMvc.perform(get(url)
                .param("memberId", memberId)
                .param("yearMonth", yearMonth)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private TodoHistoryResponseDto.Info createTodoHistoryInfo(LocalDate date){
        return TodoHistoryResponseDto.Info.builder()
                .id(1L)
                .count(1L)
                .completedCount(0L)
                .step(0)
                .dueDate(date)
                .build();
    }
}
