package com.momodo.emojihistory;

import com.momodo.emojihistory.dto.EmojiHistoryResponseDto;
import com.momodo.todo.dto.TodoResponseDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmojiHistoryController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(roles = {"MEMBER"})
public class EmojiHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmojiHistoryService emojiHistoryService;

    private Long memberId = 1L;

    @Test
    @DisplayName("EmojiHistories MemberId로 조회")
    public void findAllByMember() throws Exception{
        // given
        String url = "/api/v1/emoji-histories";
        List<EmojiHistoryResponseDto.Info> emojiHistoryInfoList = Collections.emptyList();
        doReturn(emojiHistoryInfoList).when(emojiHistoryService).findAllByMember(memberId);

        // when & then
        mockMvc.perform(get(url)
                .param("memberId", memberId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
