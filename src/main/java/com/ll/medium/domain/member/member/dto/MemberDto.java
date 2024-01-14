package com.ll.medium.domain.member.member.dto;

import com.ll.medium.domain.member.member.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberDto {
    private final Long id;
    private final String username;
    private final String nickname;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
    }

}
