package com.example.community.controller.member;


import com.example.community.config.guard.Login;
import com.example.community.domain.member.Member;
import com.example.community.dto.member.MemberEditRequestDto;
import com.example.community.response.Response;
import com.example.community.service.member.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Api(value = "Member Controller", tags = "Member")
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "회원 전체 조회", notes = "회원 전체 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members")
    public Response findAllMembers() {
        return Response.success(memberService.findAllMembers());
    }

    @ApiOperation(value = "회원 개별 조회", notes = "회원 개별 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members/{id}")
    public Response findMember(@ApiParam(value = "Member Id", required = true)
                               @PathVariable("id") Long id) {
        return Response.success(memberService.findMember(id));
    }


    @ApiOperation(value = "회원 정보 수정", notes = "회원 정보 수정")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/members")
    public Response editMemberInfo(@RequestBody MemberEditRequestDto memberEditRequestDto,
                                   @Login Member member) {
        memberService.editMemberInfo(member, memberEditRequestDto);

        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getUsername(),
            memberEditRequestDto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return Response.success();
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴 시킴")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/members")
    public Response deleteMemberInfo(@Login Member member) {
        memberService.deleteMember(member);
        return Response.success();
    }
}
