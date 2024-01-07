package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rsData.RsData.RsData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

//메서드에 @ResponsBody자동으로 붙는다
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ApiV1PostsController {
    private final PostService postService;
    @Getter
    public static class GetPostsResponseBody{
        private final List<PostDto> result;
        private final Map pagination;
        public GetPostsResponseBody(List<Post> posts) {
            result = posts.stream()
                    .map(PostDto::new)
                    .toList();

            pagination = Map.of("page", 1);
        }
    }

    @GetMapping("")
    public RsData<GetPostsResponseBody> getPosts(){
        return RsData.of("200", "success", new GetPostsResponseBody(postService.findAllByOrderByIdDesc()));
    }
}
