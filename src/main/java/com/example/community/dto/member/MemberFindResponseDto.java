package com.example.community.dto.member;

import com.example.community.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberFindResponseDto {

    private String username;
    private String name;

    public static MemberFindResponseDto toDto(Member member) {
        return new MemberFindResponseDto(member.getUsername(), member.getName());
    }
}
