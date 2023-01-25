package com.example.community.controller.comment;

import com.example.community.domain.member.Member;
import com.example.community.dto.comment.CommentCreateRequestDto;
import com.example.community.dto.comment.CommentEditRequestDto;
import com.example.community.dto.comment.CommentReadNumber;
import com.example.community.exception.MemberNotFoundException;
import com.example.community.repository.member.MemberRepository;
import com.example.community.response.Response;
import com.example.community.service.comment.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Comment Controller", tags = "Comment ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "댓글 조회", notes = "댓글을 조회합니다.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response findAllComments(@Valid CommentReadNumber commentReadNumber){
        return Response.success(commentService.findAllComments(commentReadNumber));
    }

    @ApiOperation(value = "댓글 작성", notes = "댓글을 작성 합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response createComment(@Valid @RequestBody CommentCreateRequestDto req) {
        commentService.createComment(req, getPrincipal());
        return Response.success();
    }

    @ApiOperation(value = "댓글 수정", notes = "댓글을 수정 합니다.")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response editComment(@Valid @RequestBody CommentEditRequestDto req, @PathVariable Long id) {
        commentService.editComment(req, getPrincipal(),id);
        return Response.success();
    }



    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제 합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteComment(@ApiParam(value = "댓글 id", required = true) @PathVariable Long id) {
        commentService.deleteComment(id, getPrincipal());
        return Response.success();
    }

    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName())
            .orElseThrow(MemberNotFoundException::new);
        return member;
    }

}
