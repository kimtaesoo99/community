package com.example.community.controller.board;

import com.example.community.domain.member.Member;
import com.example.community.dto.board.BoardCreateRequestDto;
import com.example.community.dto.board.BoardUpdateRequestDto;
import com.example.community.exception.MemberNotFoundException;
import com.example.community.repository.member.MemberRepository;
import com.example.community.response.Response;
import com.example.community.service.board.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Board Controller", tags = "Board")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class BoardController {

    private final MemberRepository memberRepository;
    private final BoardService boardService;


    @ApiOperation(value = "게시글 생성", notes = "게시글을 작성합니다.")
    @PostMapping("/boards")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createBoard(@Valid @ModelAttribute BoardCreateRequestDto req) {
        boardService.createBoard(req, getPrincipal());
        return Response.success();
    }

    @ApiOperation(value = "게시글 전체 조회" , notes = "게시글 전체를 조회합니다.")
    @GetMapping("/boards")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllBoards(@RequestParam(defaultValue = "0") Integer page){
        return Response.success(boardService.findAllBoards(page));
    }

    @ApiOperation(value = "게시글 단건 조회" , notes = "게시글을 단건 조회합니다.")
    @GetMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findBoard(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id){
        return Response.success(boardService.findBoard(id));
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정합니다.")
    @PutMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response editBoard(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id,
                              @Valid @ModelAttribute BoardUpdateRequestDto req) {
        Member member = getPrincipal();
        boardService.editBoard(id, req, member);
        return Response.success();
    }

    @ApiOperation(value = "게시글 삭제" ,notes = "게시글을 삭제합니다.")
    @DeleteMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteBoard(@ApiParam(value = "게시글 id", readOnly = true) @PathVariable Long id){
        boardService.deleteBoard(id, getPrincipal());
        return Response.success();
    }

    @ApiOperation(value = "게시글 검색" , notes = "게시글을 검색합니다.")
    @GetMapping("/boards/search/{keyword}")
    @ResponseStatus(HttpStatus.OK)
    public Response searchBoard(@PathVariable String keyword,@RequestParam(defaultValue = "0") Integer page){
        return Response.success(boardService.searchBoard(keyword, page));
    }


    public Member getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername(authentication.getName())
            .orElseThrow(MemberNotFoundException::new);
    }
}
