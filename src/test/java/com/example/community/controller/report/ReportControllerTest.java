package com.example.community.controller.report;

import com.example.community.config.guard.LoginMemberArgumentResolver;
import com.example.community.domain.member.Member;
import com.example.community.dto.report.BoardReportRequestDto;
import com.example.community.dto.report.CommentReportRequestDto;
import com.example.community.dto.report.MemberReportRequestDto;
import com.example.community.service.report.ReportService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {
    @InjectMocks
    ReportController reportController;

    @Mock
    ReportService reportService;

    @Mock
    LoginMemberArgumentResolver loginMemberArgumentResolver;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {

        mockMvc = MockMvcBuilders.standaloneSetup(reportController)
            .setCustomArgumentResolvers(loginMemberArgumentResolver).build();
    }

    @Test
    public void 유저신고_테스트() throws Exception {
        // given
        MemberReportRequestDto req = new MemberReportRequestDto(1L, "내용");

        Member member = createMember();
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);


        // when, then
        mockMvc.perform(
                post("/api/reports/members")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk());

        verify(reportService).reportMember(member, req);
    }

    @Test
    public void 게시판신고_테스트() throws Exception {
        // given
        BoardReportRequestDto req = new BoardReportRequestDto(1L, "내용");

        Member member = createMember();
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when, then
        mockMvc.perform(
                post("/api/reports/boards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk());

        verify(reportService).reportBoard(member, req);
    }

    @Test
    public void 댓글신고_테스트() throws Exception {
        // given
        CommentReportRequestDto req = new CommentReportRequestDto(1L, "내용");

        Member member = createMember();
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when, then
        mockMvc.perform(
                post("/api/reports/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk());

        verify(reportService).reportComment(member, req);
    }
}
