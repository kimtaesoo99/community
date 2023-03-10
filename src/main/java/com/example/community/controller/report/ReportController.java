package com.example.community.controller.report;

import com.example.community.domain.member.Member;
import com.example.community.domain.report.CommentReport;
import com.example.community.dto.report.BoardReportRequestDto;
import com.example.community.dto.report.CommentReportRequestDto;
import com.example.community.dto.report.MemberReportRequestDto;
import com.example.community.exception.MemberNotFoundException;
import com.example.community.repository.member.MemberRepository;
import com.example.community.response.Response;
import com.example.community.service.report.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Report Controller", tags = "Report")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "게시글 신고", notes = "게시글을 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/boards")
    public Response reportBoard(@Valid @RequestBody BoardReportRequestDto req) {
        return Response.success(reportService.reportBoard(getPrincipal(), req));
    }

    @ApiOperation(value = "유저 신고", notes = "유저를 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/members")
    public Response reportMember(@Valid @RequestBody MemberReportRequestDto req) {
        return Response.success(reportService.reportMember(getPrincipal(), req));
    }

    @ApiOperation(value = "댓글 신고", notes = "댓글을 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/comments")
    public Response reportComment(@Valid @RequestBody CommentReportRequestDto req) {
        return Response.success(reportService.reportComment(getPrincipal(), req));
    }

    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        return member;
    }
}
