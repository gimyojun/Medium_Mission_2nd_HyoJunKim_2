package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq.Rq;
import com.ll.medium.global.rsData.RsData.RsData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//메서드에 @ResponsBody자동으로 붙는다
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ApiV1PostsController {
    private final PostService postService;
    private final Rq rq;
    private final MemberService memberService;
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
    @Getter
    public static class GetPostResponseBody{
        private final PostDto result;
        public GetPostResponseBody(Post post) {
            result = new PostDto(post);
        }
    }
    @GetMapping("/{id}")
    public RsData<GetPostResponseBody> getPost(@PathVariable Long id){
        return RsData.of("200", "success", new GetPostResponseBody(postService.findById(id).get()));
    }

    @Getter
    public static class DeletePostResponseBody{
        private final PostDto result;
        public DeletePostResponseBody(Post post) {
            result = new PostDto(post);
        }
    }
    @DeleteMapping("/{id}")
    public RsData<DeletePostResponseBody> deletePost(@PathVariable Long id){
        RsData<Post> rsData = postService.deletePost(id);
        return RsData.of(
                rsData.getResultCode(),
                rsData.getMsg(),
                new DeletePostResponseBody(
                        rsData.getData()
                )
        );
    }
    @Getter
    @Setter
    public static class UpdatePostRequestBody{
        private String title;
        private String body;
    }

    @Getter
    public static class UpdatePostResponseBody{
        private final PostDto result;
        public UpdatePostResponseBody(Post post) {
            result = new PostDto(post);
        }
    }
    @PutMapping("/{id}")
    public RsData<UpdatePostResponseBody> updatePost(@PathVariable Long id, @RequestBody UpdatePostRequestBody body){
        Post post = postService.findById(id).get();
        RsData<Post> rsData = postService.updatePost(post, body.getTitle(), body.getBody());

        return RsData.of(rsData.getResultCode(), rsData.getMsg(), new UpdatePostResponseBody(rsData.getData()));
    }
    @Getter
    @Setter
    public static class WritePostRequestBody{
        private String title;
        private String body;
    }

    @Getter
    public static class WritePostResponseBody{
        private final PostDto result;
        public WritePostResponseBody(Post post) {
            result = new PostDto(post);
        }
    }
    @PostMapping("")
    public RsData<WritePostResponseBody> writePost(@RequestBody WritePostRequestBody body){
        Member member = rq.getLoginedMember();
        //임시 TODO 로그인 안된경우 writePost를 못하게 막아야함. 지금 임시로 3번 member 할당함
        if(member == null){
            member = memberService.findById(3L).get();
        }

        RsData<Post> rsData= postService.writePost(member, body.getTitle(), body.getBody());

        return rsData.of(
                new WritePostResponseBody(rsData.getData())
        );
    }


}
