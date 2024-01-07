package com.ll.medium.domain.member.member.entity;

import com.ll.medium.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@SuperBuilder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Member extends BaseEntity {

    private String username;

    private String password;

    private boolean isPaid;

    private String nickname;

    public String getName() {
        return this.nickname != null ? this.nickname : this.username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        if (List.of("system", "admin").contains(username)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            //어드민은 유료 멤버십 글을 볼 수 있어야한다.
            authorities.add(new SimpleGrantedAuthority("ROLE_PAID"));
        }
        if(this.isPaid()){
            authorities.add(new SimpleGrantedAuthority("ROLE_PAID"));
        }

        return authorities;
    }


}
