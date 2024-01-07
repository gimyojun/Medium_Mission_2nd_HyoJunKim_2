package com.ll.medium.domain.post.post.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ApiV1PostsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("get /api/v1/posts")
    void t1() throws Exception{
        //given

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/posts"))
                        .andDo(print());
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPosts"))
                .andExpect(jsonPath("$.data.result[0].id", is(100)))
                .andExpect(jsonPath("$.data.result[0].title", is("제목 100")))
                .andDo(print());
    }




}
