package com.example.community.dto.admin;

import com.example.community.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportedMemberFindAllResponseDto {

    private Long id;
    private String username;
    private String name;

    public ReportedMemberFindAllResponseDto toDto(Member member) {
        return new ReportedMemberFindAllResponseDto(member.getId(),member.getUsername(), member.getName());
    }
}

