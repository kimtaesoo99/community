package com.example.community.controller.member;


import com.example.community.config.guard.LoginMemberArgumentResolver;
import com.example.community.domain.member.Member;
import com.example.community.dto.member.MemberEditRequestDto;
import com.example.community.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.example.community.factory.MemberFactory.createMember;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {
    @InjectMocks
    MemberController memberController;

    @Mock
    MemberService memberService;

    @Mock
    LoginMemberArgumentResolver loginMemberArgumentResolver;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
            .setCustomArgumentResolvers(loginMemberArgumentResolver)
            .build();
    }

    @Test
    public void 회원전체조회테스트() throws Exception {
        mockMvc.perform(
                get("/api/members"))
            .andExpect(status().isOk());
        verify(memberService).findAllMembers();
    }

    @Test
    public void 회원단건조회테스트() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(
                get("/api/members/{id}", id))
            .andExpect(status().isOk());
        verify(memberService).findMember(id);
    }


    @Test
    public void 회원정보수정테스트() throws Exception {
        // given
        MemberEditRequestDto req = new MemberEditRequestDto("비밀번호수정", "이름 수정");
        Member member = createMember();
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when
        mockMvc.perform(
            put("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isOk());

        // then
        verify(memberService).editMemberInfo(refEq(member), refEq(req));
    }

    @Test
    public void 회원탈퇴테스트() throws Exception {
        // given
        Member member = createMember();
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when then
        mockMvc.perform(
                delete("/api/members"))
            .andExpect(status().isOk());

        verify(memberService).deleteMember(member);

    }
}
