package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/{id}")
    public String showDetail(@PathVariable long id) {

        Optional<Post> post = postService.findById(id);
        if(post.isEmpty())
            throw new RuntimeException("포스트를 찾을 수 없습니다.");

        Post myPost = post.get();
        if (myPost.isPaid()) {
            if (!rq.isLogin()) return rq.redirect("/post/list","유료 멤버십 컨텐츠는 로그인 후 이용 가능합니다.");
            if (!rq.isPaid()) return rq.redirect("/post/list","유료 멤버십이 필요한 컨텐츠입니다. 멤버십을 구매해주세요.");
        }
        postService.hitPost(myPost);

        rq.setAttribute("post", myPost);
        return "domain/post/post/detail";

    }

    @GetMapping("/list")
    public String showList(
            @RequestParam(value = "kwType", defaultValue = "title,body") List<String> kwTypes,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(value = "sortCode", defaultValue = "idDesc") String sortCode
    ) {
        List<Sort.Order> sorts = new ArrayList<>();

        switch (sortCode) {
            case "idDesc":
                sorts.add(Sort.Order.desc("id"));
                break;
            case "idAsc":
                sorts.add(Sort.Order.asc("id"));
                break;
            case "hitCountDesc":
                sorts.add(Sort.Order.desc("hitCount"));
                break;
            case "likeCountDesc":
                sorts.add(Sort.Order.desc("likeCount"));
                break;
            // 추가적인 정렬 조건이 필요하면 여기에 추가
        }

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

        Page<Post> postPage = postService.search(kwTypes ,kw, pageable);

        Map<String, Boolean> kwTypesMap = kwTypes
                .stream()
                .collect(Collectors.toMap(
                        kwType -> kwType,
                        kwType -> true
                ));

        rq.setAttribute("postPage", postPage);
        rq.setAttribute("page", page);
        rq.setAttribute("kwTypesMap", kwTypesMap);
        return "domain/post/post/list";
    }
    @Setter
    @Getter
    public static class postForm {

        @NotEmpty(message = "제목을 입력해주세요")
        private String title;

        @NotEmpty(message = "내용을 입력해주세요")
        private String body;

        private boolean published;

        private boolean paid;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String create(postForm postForm) {
        return "domain/post/post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String createQuestion(@Valid postForm postForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "domain/post/post/write";
        }

        Member member = rq.getLoginedMember();
        if (postForm.isPaid() && !member.isPaid())
            throw new RuntimeException("멤버십 포스트는 구독 후 게시할 수 있습니다.");

        postService.write(member, postForm.getTitle(), postForm.getBody(),postForm.isPublished(),postForm.isPaid());
        return rq.redirect("/post/list", "포스트가 정상적으로 등록되었습니다.");

    }

    @GetMapping("/{id}/like")
    public String like(@PathVariable Long id) {
        Post post = postService.likePost(id);
        rq.setAttribute("post", post);
        return "domain/post/post/detail";
    }


}
