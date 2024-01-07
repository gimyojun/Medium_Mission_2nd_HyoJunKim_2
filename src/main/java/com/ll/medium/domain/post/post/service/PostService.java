package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public void write(Member author, String title, String body, boolean isPublished, boolean isPaid) {
        Post post = Post.builder()
                .author(author)
                .title(title)
                .body(body)
                .isPublished(isPublished)
                .isPaid(isPaid)
                .hitCount(0L)
                .likeCount(0L)
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

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findAllByOrderByIdDesc() {
        return postRepository.findAllByOrderByIdDesc();
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public void updatePost(Post post, String title, String body) {
        post.setTitle(title);
        post.setBody(body);
        //더티 체킹으로 생략 가능
        //postRepository.save(post);
    }
}
