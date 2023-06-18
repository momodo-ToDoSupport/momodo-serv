package com.momodo.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.dto.TodoResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private ObjectMapper mapper;

    @BeforeEach
    public void init(){
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("Todo 등록")
    public void create() throws Exception{
        // given
        String url = "/todos";
        TodoRequestDto.Create todoRequest = todoRequestDto();
        doReturn(todoRequestDto().toEntity()).when(todoService).createTodo(todoRequest);

        // when & then
        mockMvc.perform(post(url)
                .content(mapper.writeValueAsString(todoRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo Id로 조회")
    public void findById() throws Exception{
        // given
        String url = "/todos/1";
        TodoResponseDto.Info todoInfo = todoResponseDto();
        doReturn(todoInfo).when(todoService).findById(todoInfo.getId());

        // when & then
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo DueDate로 조회")
    public void findAllByDueDate() throws Exception{
        // given
        LocalDate date = LocalDate.now();
        String url = "/todos";
        List<TodoResponseDto.Info> todoInfoList = Collections.emptyList();
        doReturn(todoInfoList).when(todoService).findAllByDueDate(date);

        // when & then
        mockMvc.perform(get(url)
                .param("dueDate", date.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo 성공 여부 수정")
    public void updateCompleted() throws Exception{
        // given
        String url = "/todos/1/updateCompleted";
        Long id = 1L;
        doNothing().when(todoService).updateCompleted(id);

        // when & then
        mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo 정보 수정")
    public void update() throws Exception{
        // given
        String url = "/todos/1/update";
        Long id = 1L;
        TodoRequestDto.Edit request = new TodoRequestDto.Edit("Edit Title", "Edit Emoji", "Edit RepeatDays");
        doNothing().when(todoService).update(id, request);

        // when & then
        mockMvc.perform(patch(url)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo 삭제")
    public void deleteById() throws Exception{
        // given
        String url = "/todos/1";
        Long id = 1L;
        doNothing().when(todoService).deleteById(id);

        // when & then
        mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private TodoRequestDto.Create todoRequestDto(){
        return TodoRequestDto.Create.builder()
                .memberId(1L)
                .title("Test Todo")
                .emoji("Test Emoji")
                .dueDate(LocalDate.now())
                .repeatDays(null)
                .build();
    }

    private TodoResponseDto.Info todoResponseDto(){
        return TodoResponseDto.Info.builder()
                .id(1L)
                .title("Test Todo")
                .emoji("Test Emoji")
                .dueDate(LocalDate.now())
                .isCompleted(false)
                .repeatDays(null)
                .build();
    }
}