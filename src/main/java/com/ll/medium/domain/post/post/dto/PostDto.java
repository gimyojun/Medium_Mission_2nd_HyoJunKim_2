package com.ll.medium.domain.post.post.dto;

import com.ll.medium.domain.post.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDto {
    private final Long id;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;
    private final Long authorId;
    private final String authorName;
    private final String title;
    private final String body;

    public PostDto(Post post){
        this.id = post.getId();
        this.createDate = post.getCreateDate();
        this.modifyDate = post.getModifyDate();
        this.authorId = post.getAuthor().getId();
        this.authorName = post.getAuthor().getName();
        this.title = post.getTitle();
        this.body = post.getBody();
    }

}
