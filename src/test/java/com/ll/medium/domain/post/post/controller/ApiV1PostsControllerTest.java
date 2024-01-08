package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional //롤백됨, 롤백 안하려면 테스트 메서드에 @Rollback(false)붙이기
@ActiveProfiles("test")
public class ApiV1PostsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostService postService;
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

    @Test
    @DisplayName("delete /api/v1/posts/{id}")
    void t3() throws Exception{
        //given

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/posts/89"))
                .andDo(print());
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("deletePost"))
                .andExpect(jsonPath("$.data.result.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.result.title", notNullValue()))
                .andExpect(jsonPath("$.data.result.body", notNullValue()))
                .andExpect(jsonPath("$.data.result.authorId",instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.result.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.result.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.result.modifyDate", matchesPattern(DATE_PATTERN)))
                .andDo(print());

        Post post = postService.findById(89L).orElse(null);
        assertThat(post).isNull();

    }
    @Test
    @DisplayName("put /api/v1/posts/{id}")
    void t4() throws Exception{
        //given

        //when
        ResultActions resultActions = mockMvc
                .perform(
                        put("/api/v1/posts/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "수정 제목 99",
                                            "body": "수정 내용 99"
                                        }
                                        """)

                )
                .andDo(print());
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("updatePost"))
                .andExpect(jsonPath("$.data.result.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.result.title", is("수정 제목 99")))
                .andExpect(jsonPath("$.data.result.body", is("수정 내용 99")))
                .andExpect(jsonPath("$.data.result.authorId",instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.result.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.result.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.result.modifyDate", matchesPattern(DATE_PATTERN)))
                .andDo(print());
    }

    @Test
    @DisplayName("post /api/v1/posts")
    void t5() throws Exception{
        //given

        //when
        ResultActions resultActions = mockMvc
                .perform(
                        post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "new 제목",
                                            "body": "new 내용"
                                        }
                                        """)

                )
                .andDo(print());
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostsController.class))
                .andExpect(handler().methodName("writePost"))
                .andExpect(jsonPath("$.data.result.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.result.title", is("new 제목")))
                .andExpect(jsonPath("$.data.result.body", is("new 내용")))
                .andExpect(jsonPath("$.data.result.authorId",instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.result.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.result.createDate", matchesPattern(DATE_PATTERN)))
                .andExpect(jsonPath("$.data.result.modifyDate", matchesPattern(DATE_PATTERN)))
                .andDo(print());
    }

}
