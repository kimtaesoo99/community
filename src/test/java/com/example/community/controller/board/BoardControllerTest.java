package com.example.community.controller.board;

import com.example.community.domain.member.Member;
import com.example.community.dto.board.BoardCreateRequestDto;
import com.example.community.dto.board.BoardUpdateRequestDto;
import com.example.community.repository.member.MemberRepository;
import com.example.community.service.board.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.community.factory.MemberFactory.createMember;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {
    @InjectMocks
    BoardController boardController;
    @Mock
    MemberRepository memberRepository;
    @Mock
    BoardService boardService;
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
    }

    @Test
    public void 게시글생성_테스트() throws Exception{
        //given
        List<MultipartFile> images = new ArrayList<>();
        images.add(new MockMultipartFile("test1","test1.png", MediaType.IMAGE_PNG_VALUE,"test1".getBytes()));
        images.add(new MockMultipartFile("test2","test2.png", MediaType.IMAGE_PNG_VALUE,"test2".getBytes()));
        BoardCreateRequestDto req = new BoardCreateRequestDto("title", "content", images);

        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when,then
        mockMvc.perform(
                multipart("/api/boards")
                    .file("images", images.get(0).getBytes())
                    .file("images", images.get(1).getBytes())
                    .param("title", req.getTitle())
                    .param("content", req.getContent())
                    .with(requestBoardProcessor -> {
                        requestBoardProcessor.setMethod("POST");
                        return requestBoardProcessor;
                    })
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated());

    }

    @Test
    public void 게시글전체조회_테스트()throws Exception{
        //given
        Integer page = 0;

        //when
        mockMvc.perform(
                get("/api/boards"))
            .andExpect(status().isOk());

        //then
        verify(boardService).findAllBoards(page);
    }

    @Test
    public void 게시글단건조회_테스트() throws Exception{
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
                get("/api/boards/{id}",id))
            .andExpect(status().isOk());


        //then
        verify(boardService).findBoard(id);
    }

    @Test
    public void 게시글수정_테스트() throws Exception{
        //given
        Long id =1L;
        List<MultipartFile> addImages = new ArrayList<>();
        addImages.add(new MockMultipartFile("test1","test1.png",MediaType.IMAGE_PNG_VALUE,"test1".getBytes()));
        List<Integer> deletedImages = List.of(1);
        BoardUpdateRequestDto req = new BoardUpdateRequestDto("title", "content", addImages, deletedImages);

        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when, then
        mockMvc.perform(
            multipart("/api/boards/{id}", id)
                .file("addedImags", addImages.get(0).getBytes())
                .param("deletedImages", String.valueOf(deletedImages.get(0)))
                .param("title", req.getTitle())
                .param("content", req.getContent())
                .with(requestBoardProcessor -> {
                    requestBoardProcessor.setMethod("PUT");
                    return requestBoardProcessor;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isOk());
    }

    @Test
    public void 게시글삭제_테스트() throws Exception{
        //given
        Long id = 1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));


        //when
        mockMvc.perform(
            delete("/api/boards/{id}", id)
        ).andExpect(status().isOk());

        //then
        verify(boardService).deleteBoard(id,member);
    }

    @Test
    public void 게시글검색_테스트() throws Exception{
        //given
        String keyword = "title";
        Integer page = 0;

        //when
        mockMvc.perform(
            get("/api/boards/search/{keyword}", keyword)
        ).andExpect(status().isOk());


        //then
        verify(boardService).searchBoard(keyword, page);
    }

    @Test
    public void 게시글좋아요_테스트() throws Exception{
        //given
        Long id =1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
            post("/api/boards/{id}", id)
        ).andExpect(status().isOk());

        //then
        verify(boardService).updateLikeBoard(id, member);
    }

    @Test
    public void 게시글즐겨찾기_테스트() throws Exception{
        //given
        Long id =1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
            post("/api/boards/{id}/favorites", id)
        ).andExpect(status().isOk());

        //then
        verify(boardService).updateFavoriteBoard(id, member);
    }

    @Test
    public void 게시글즐겨찾기목록조회_테스트() throws Exception{
        //given
        Integer page =0;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
            get("/api/boards/favorites")
        ).andExpect(status().isOk());

        //then
        verify(boardService).findFavoriteBoards(page,member);
    }

    @Test
    public void 게시글좋아요순으로조회_테스트() throws Exception{
        //given
        Integer page = 0;

        //when
        mockMvc.perform(
                get("/api/boards/likes"))
            .andExpect(status().isOk());

        //then
        verify(boardService).findAllBoardsWithLikes(page);
    }
}
