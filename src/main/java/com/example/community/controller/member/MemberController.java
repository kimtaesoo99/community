package com.example.community.controller.member;


import com.example.community.service.member.MemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Member Controller", tags = "Member")
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


}
