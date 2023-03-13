package com.example.community.factory;

import com.example.community.domain.member.Authority;
import com.example.community.domain.member.Member;

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

    public static Member createMember2(){
        Member member = Member.builder()
            .username("username2")
            .name("name2")
            .password("12456")
            .authority(Authority.ROLE_USER)
            .build();
        return member;
    }

    public static Member createMemberWithId(Long id){
        return new Member(id,"username","1234","name",Authority.ROLE_USER);
    }

    public static Member createMemberWithUsername(String username){
        return new Member(1l,username,"1234","name",Authority.ROLE_USER);
    }

}
