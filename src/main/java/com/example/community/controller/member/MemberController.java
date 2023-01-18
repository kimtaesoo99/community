package com.example.community.controller.member;


import com.example.community.repository.member.MemberRepository;
import com.example.community.response.Response;
import com.example.community.service.member.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "Member Controller", tags = "Member")
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "회원 전체 조회", notes = "회원 전체 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members")
    public Response findAllMembers(){
        return Response.success(memberService.findAllMembers());
    }

    @ApiOperation(value = "회원 개별 조회", notes = "회원 개별 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members/{id}")
    public Response findMember(@ApiParam(value = "Member Id",required = true)
                                   @PathVariable("id") Long id){
        return Response.success(memberService.findMember(id));
    }


}
