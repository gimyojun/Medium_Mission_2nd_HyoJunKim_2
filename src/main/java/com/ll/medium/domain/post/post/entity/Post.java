package com.ll.medium.domain.post.post.entity;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Entity
@SuperBuilder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Post extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    private String title;

    private String body;

    private boolean isPublished;

    private boolean isPaid;

    @Column(nullable = false)
    private Long hitCount = 0L;

    @Column(nullable = false)
    private Long likeCount = 0L;
}
