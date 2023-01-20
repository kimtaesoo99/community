package com.example.community.service.member;

import com.example.community.domain.member.Member;
import com.example.community.dto.member.MemberEditRequestDto;
import com.example.community.dto.member.MemberFindResponseDto;
import com.example.community.exception.MemberNotFoundException;
import com.example.community.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<MemberFindResponseDto> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
            .map(MemberFindResponseDto::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberFindResponseDto findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberFindResponseDto.toDto(member);
    }

    @Transactional
    public void editMemberInfo(Member member,MemberEditRequestDto memberEditRequestDto) {
        member.modify(
            passwordEncoder.encode(memberEditRequestDto.getPassword()),
            memberEditRequestDto.getName());
    }

    @Transactional
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

}
