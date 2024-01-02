package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;


    public void write(Member author, String title, String body, boolean isPublished, boolean isPaid) {
        Post post = Post.builder()
                .author(author)
                .title(title)
                .body(body)
                .isPublished(isPublished)
                .isPaid(isPaid)
                .build();

        postRepository.save(post);
    }

    public Object findTop30ByIsPublishedOrderByIdDesc(boolean isPublished) {
        return postRepository.findTop30ByIsPublishedOrderByIdDesc(isPublished);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public Page<Post> search(List<String> kwTypes, String kw, Pageable pageable) {
        return postRepository.search(kwTypes, kw, pageable);
    }

    public Post likePost(Long id) {
        Optional<Post> op = postRepository.findById(id);
        if (op.isPresent()) {
            Post post = op.get();
            Long currentLikeCount = post.getLikeCount();
            post.setLikeCount(currentLikeCount + 1);
            return postRepository.save(post);
        }else {
            throw new RuntimeException("like count 올릴수 없음");
        }

    }


    public void hitPost(Post myPost) {
        Long currentHitCount = myPost.getHitCount();
        myPost.setHitCount(currentHitCount + 1);
        postRepository.save(myPost);

    }


}
