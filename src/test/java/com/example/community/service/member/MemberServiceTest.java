package com.example.community.service.member;

import com.example.community.domain.member.Member;
import com.example.community.dto.member.MemberEditRequestDto;
import com.example.community.dto.member.MemberFindResponseDto;
import com.example.community.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.example.community.factory.MemberFactory.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void 회원전체조회테스트() {
        // given
        Member member = createMember();
        Member member2 = createMember();
        List<Member> list = new LinkedList<>();
        list.add(member);
        list.add(member2);

        given(memberRepository.findAll()).willReturn(list);

        // when
        List<MemberFindResponseDto> result = memberService.findAllMembers();

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void 회원단건조회테스트() {
        // given
        Long id = 1L;
        Member member = createMember();

        given(memberRepository.findById(id)).willReturn(Optional.of(member));

        // when
        MemberFindResponseDto result = memberService.findMember(id);

        // then
        assertThat(result.getUsername()).isEqualTo("username");
    }

    @Test
    public void 회원정보수정테스트() {
        //given
        Member member = createMember();
        MemberEditRequestDto req = new MemberEditRequestDto("0000", "new");

        //when
        memberService.editMemberInfo(member,req);

        //then
        assertThat(member.getName()).isEqualTo("new");
    }

    @Test
    public void 회원삭제테스트() {
        //given
        Member member = createMember();

        //when
        memberService.deleteMember(member);

        //then
        verify(memberRepository).delete(any());
    }

}
