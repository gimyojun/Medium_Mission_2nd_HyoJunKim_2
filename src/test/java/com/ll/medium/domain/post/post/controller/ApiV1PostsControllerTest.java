package com.ll.medium.domain.post.post.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ApiV1PostsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.?\\d{0,7}";
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
                .andExpect(jsonPath("$.data.result[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.result[0].title", notNullValue()))
                .andExpect(jsonPath("$.data.result[0].body", notNullValue()))
                .andExpect(jsonPath("$.data.result[0].authorId",instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.result[0].authorName", notNullValue()))
                .andExpect(jsonPath("$.data.result[0].createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.result[0].modifyDate", matchesPattern(DATE_PATTERN)))
                .andDo(print());
    }
    @Test
    @DisplayName("get /api/v1/posts/{id}")
    void t2() throws Exception{
        //given

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/posts/100"))
                .andDo(print());
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("getPost"))
                .andExpect(jsonPath("$.data.result.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.result.title", notNullValue()))
                .andExpect(jsonPath("$.data.result.body", notNullValue()))
                .andExpect(jsonPath("$.data.result.authorId",instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.result.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.result.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.result.modifyDate", matchesPattern(DATE_PATTERN)))
                .andDo(print());
    }



}
