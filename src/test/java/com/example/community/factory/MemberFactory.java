package com.example.community.factory;

import com.example.community.entity.member.Authority;
import com.example.community.entity.member.Member;

public class MemberFactory {

    public static Member createMember(){
        Member member = Member.builder()
            .username("username")
            .name("name")
            .password("1245")
            .authority(Authority.ROLE_USER)
            .build();
        return member;
    }
}
