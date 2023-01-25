package com.example.community.controller.comment;

import com.example.community.domain.member.Member;
import com.example.community.dto.comment.CommentCreateRequestDto;
import com.example.community.dto.comment.CommentEditRequestDto;
import com.example.community.dto.comment.CommentReadNumber;
import com.example.community.repository.member.MemberRepository;
import com.example.community.service.comment.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static com.example.community.factory.MemberFactory.createMember;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {
    @InjectMocks
    CommentController commentController;

    @Mock
    MemberRepository memberRepository;

    @Mock
    CommentService commentService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    public void 댓글작성_테스트() throws Exception {
        // given
        CommentCreateRequestDto req = new CommentCreateRequestDto(1L, "content");

        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
            Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when then
        mockMvc.perform(
                post("/api/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated());

        verify(commentService).createComment(req, member);
    }


    @Test
    public void 댓글조회_테스트() throws Exception {
        // given
        CommentReadNumber req = new CommentReadNumber(1L);

        // when, then
        mockMvc.perform(
                get("/api/comments")
                    .param("boardId", String.valueOf(req.getBoardId()))
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk());

        verify(commentService).findAllComments(req);
    }
    @Test

    public void 댓글수정_테스트() throws Exception {
        // given
        Long id = 1L;
        CommentEditRequestDto req = new CommentEditRequestDto("hi");

        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
            Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));


        // when, then
        mockMvc.perform(
                put("/api/comments/{id}",id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req))
            ).andExpect(status().isOk());

        verify(commentService).editComment(req,member,id);
    }

    @Test
    @DisplayName("댓글 삭제")
    public void deleteTest() throws Exception {
        // given
        Long id = 1L;

        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
            Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when, then
        mockMvc.perform(
                delete("/api/comments/{id}", id))
            .andExpect(status().isOk());
        verify(commentService).deleteComment(id, member);
    }
}
