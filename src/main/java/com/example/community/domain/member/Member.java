package com.example.community.domain.member;

import com.example.community.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private boolean isReported;

    @Builder
    public Member(Long id, String username, String password, String name, Authority authority) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.authority = authority;
        this.isReported = false;
    }

    public void modify(String password,String name) {
        this.password = password;
        this.name = name;
        onPreUpdate();
    }

    public void isReportedStatus(){
        isReported = true;
        authority = Authority.ROLE_SUSPENSION;
    }

    public void unLockedReportedStatus(){
        isReported = false;
        authority = Authority.ROLE_USER;
    }
}
