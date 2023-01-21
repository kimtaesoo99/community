package com.example.community.service.message;


import com.example.community.domain.member.Member;
import com.example.community.domain.message.Message;
import com.example.community.dto.message.MessageCreateRequestDto;
import com.example.community.dto.message.MessageFindAllResponseDto;
import com.example.community.dto.message.MessageFindResponseDto;
import com.example.community.repository.member.MemberRepository;
import com.example.community.repository.message.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.community.factory.MemberFactory.createMember;
import static com.example.community.factory.MessageFactory.createMessage;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @InjectMocks
    MessageService messageService;

    @Mock
    MessageRepository messageRepository;

    @Mock
    MemberRepository memberRepository;


    @Test
    public void 쪽지보내기테스트() throws Exception{
        //given
        Member sender = Member.builder()
            .username("sender")
            .build();
        Member member = createMember();
        MessageCreateRequestDto req = new MessageCreateRequestDto("title", "content", "username");
        given(memberRepository.findByUsername(req.getReceiverUsername())).willReturn(Optional.of(member));

        //when
        messageService.sendMessage(sender,req);

        //then
        verify(messageRepository).save(any());
    }


    @Test
    public void 받은쪽지_전체조회테스트() throws Exception{
        //given
        Long id = 1L;
        Member sender = Member.builder()
            .username("sender")
            .build();
        Member receiver = createMember();
        Message message = createMessage(id,sender,receiver);
        receiver.receiveMessage(message);

        //when
        List<MessageFindAllResponseDto> result = messageService
            .findALlReceiveMessages(receiver);

        //then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void 받은쪽지_단건조회테스트() throws Exception{
        //given
        Long id = 1L;
        Member sender = Member.builder()
            .username("sender")
            .build();
        Member receiver = createMember();
        Message message = createMessage(id,sender,receiver);
        receiver.receiveMessage(message);

        //when
        MessageFindResponseDto result = messageService
            .findReceiveMessage(receiver,id);

        //then
        assertThat(result.getReceiverName()).isEqualTo(receiver.getUsername());
    }

    @Test
    public void 보낸쪽지_전체조회테스트() throws Exception{
        //given
        Long id = 1L;
        Member sender = Member.builder()
            .username("sender")
            .build();
        Member receiver = createMember();
        Message message = createMessage(id,sender,receiver);
        sender.sendMessage(message);

        //when
        List<MessageFindAllResponseDto> result = messageService
            .findAllSendMessages(sender);

        //then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void 보낸쪽지_단건조회테스트() throws Exception{
        //given
        Long id = 1L;
        Member sender = Member.builder()
            .username("sender")
            .build();
        Member receiver = createMember();
        Message message = createMessage(id,sender,receiver);
        sender.sendMessage(message);

        //when
        MessageFindResponseDto result = messageService
            .findSendMessage(sender,id);

        //then
        assertThat(result.getSenderName()).isEqualTo(sender.getUsername());
    }


    @Test
    public void 받은편지_삭제테스트() throws Exception{
        //given
        Long id = 1L;
        Member sender = Member.builder()
            .username("sender")
            .build();
        Member receiver = createMember();
        Message message = createMessage(id,sender,receiver);
        receiver.receiveMessage(message);

        //when
        messageService.deleteReceiverMessage(receiver,id);

        //then
        assertThat(message.isDeletedByReceiver()).isTrue();
    }

    @Test
    public void 보낸편지_삭제테스트() throws Exception{
        //given
        Long id = 1L;
        Member sender = Member.builder()
            .username("sender")
            .build();
        Member receiver = createMember();
        Message message = createMessage(id,sender,receiver);
        sender.sendMessage(message);

        //when
        messageService.deleteSenderMessage(sender,id);

        //then
        assertThat(message.isDeletedBySender()).isTrue();
    }

    @Test
    public void 편지양쪽_삭제테스트() throws Exception{
        //given
        Long id = 1L;
        Member sender = Member.builder()
            .username("sender")
            .build();
        Member receiver = createMember();
        Message message = createMessage(id,sender,receiver);
        sender.sendMessage(message);
        receiver.receiveMessage(message);

        //when
        messageService.deleteSenderMessage(sender,id);
        messageService.deleteReceiverMessage(receiver,id);

        //then
        verify(messageRepository).delete(message);
    }

}
