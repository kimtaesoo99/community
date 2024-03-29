package com.example.community.controller.message;


import com.example.community.config.guard.LoginMemberArgumentResolver;
import com.example.community.domain.member.Member;
import com.example.community.dto.message.MessageCreateRequestDto;
import com.example.community.service.message.MessageService;
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
class MessageControllerTest {

    @InjectMocks
    MessageController messageController;

    @Mock
    MessageService messageService;

    @Mock
    LoginMemberArgumentResolver loginMemberArgumentResolver;


    ObjectMapper objectMapper = new ObjectMapper();

    MockMvc mockMvc;


    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController)
            .setCustomArgumentResolvers(loginMemberArgumentResolver
            ).build();
    }

    @Test
    public void 메세지보내기테스트() throws Exception {
        //given
        Member member = createMember();
        MessageCreateRequestDto req = new MessageCreateRequestDto("title", "content", "username");
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        //when
        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))

            .andExpect(status().isCreated());

        //then
        verify(messageService).sendMessage(refEq(member), refEq(req));
    }


    @Test
    public void 받은쪽지_전체조회테스트() throws Exception {
        // given

        Member member = createMember();
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when
        mockMvc.perform(
                get("/api/messages/receiver"))
            .andExpect(status().isOk());

        //then
        verify(messageService).findALlReceiveMessages(member);
    }

    @Test
    public void 받은쪽지_단건조회테스트() throws Exception {
        // given
        Long id = 1L;

        Member member = createMember();
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when
        mockMvc.perform(
                get("/api/messages/receiver/{id}", id))
            .andExpect(status().isOk());

        //then
        verify(messageService).findReceiveMessage(member,id);
    }

    @Test
    public void 보낸쪽지_전체조회테스트() throws Exception {
        // given
        Member member = createMember();
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when
        mockMvc.perform(
                get("/api/messages/sender"))
            .andExpect(status().isOk());

        //then
        verify(messageService).findAllSendMessages(member);
    }

    @Test
    public void 보낸쪽지_단건조회테스트() throws Exception {
        // given
        Long id = 1L;

        Member member = createMember();
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when
        mockMvc.perform(
                get("/api/messages/sender/{id}", id))
            .andExpect(status().isOk());

        //then
        verify(messageService).findSendMessage(member,id);
    }

    @Test
    public void 받은쪽지_삭제테스트() throws Exception {
        // given
        Long id = 1L;

        Member member = createMember();
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when
        mockMvc.perform(
                delete("/api/messages/receiver/{id}", id))
            .andExpect(status().isOk());

        //then
        verify(messageService).deleteReceiverMessage(member,id);
    }

    @Test
    public void 보낸쪽지_삭제테스트() throws Exception {
        // given
        Long id = 1L;

        Member member = createMember();
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id))
            .andExpect(status().isOk());

        //then
        verify(messageService).deleteSenderMessage(member,id);
    }



}
