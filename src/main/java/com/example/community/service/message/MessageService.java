package com.example.community.service.message;

import com.example.community.domain.member.Member;
import com.example.community.domain.message.Message;
import com.example.community.dto.message.MessageCreateRequestDto;
import com.example.community.dto.message.MessageFindAllResponseDto;
import com.example.community.dto.message.MessageFindResponseDto;
import com.example.community.exception.MemberNotFoundException;
import com.example.community.exception.MessageNotFoundException;
import com.example.community.repository.member.MemberRepository;
import com.example.community.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public void sendMessage(Member sender, MessageCreateRequestDto messageCreateRequestDto){
        Member receiver = memberRepository
            .findByUsername(messageCreateRequestDto.getReceiverUsername())
            .orElseThrow(MemberNotFoundException::new);
        Message message = new Message(messageCreateRequestDto.getTitle(),
            messageCreateRequestDto.getContent(),
            sender,
            receiver);
        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<MessageFindAllResponseDto> findALlReceiveMessages(Member member){
        List<Message> messages = messageRepository.findAllByReceiverQuery(member.getUsername());
        return messages.stream().map(MessageFindAllResponseDto::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MessageFindResponseDto findReceiveMessage(Member member,Long id){
        Message message = messageRepository.findByIdWithReceiver(id, member.getUsername())
            .orElseThrow(MessageNotFoundException::new);
        return MessageFindResponseDto.toDto(message);

    }

    @Transactional(readOnly = true)
    public List<MessageFindAllResponseDto> findAllSendMessages(Member member){
        List<Message> messages = messageRepository.findAllBySenderQuery(member.getUsername());
        return messages.stream().map(MessageFindAllResponseDto::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MessageFindResponseDto findSendMessage(Member member,Long id){
        Message message = messageRepository.findByIdWithSender(id, member.getUsername())
            .orElseThrow(MessageNotFoundException::new);
        return MessageFindResponseDto.toDto(message);
    }

    @Transactional
    public void deleteReceiverMessage(Member member,Long id){
        Message message = messageRepository.findByIdWithReceiver(id, member.getUsername())
            .orElseThrow(MessageNotFoundException::new);
        message.deleteByReceiver();
        if (message.isDeletedBySender() && message.isDeletedByReceiver()){
            messageRepository.delete(message);
        }
    }


    @Transactional
    public void deleteSenderMessage(Member member,Long id){
        Message message = messageRepository.findByIdWithSender(id, member.getUsername())
            .orElseThrow(MessageNotFoundException::new);
        message.deleteBySender();
        if (message.isDeletedBySender()&& message.isDeletedByReceiver()){
            messageRepository.delete(message);
        }
    }
}
