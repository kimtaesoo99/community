package com.example.community.dto.message;

import com.example.community.domain.message.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageFindResponseDto {
    private String title;
    private String content;
    private String senderName;
    private String receiverName;

    public static MessageFindResponseDto toDto(Message message) {
        return new MessageFindResponseDto(
            message.getTitle(),
            message.getContent(),
            message.getSender().getUsername(),
            message.getReceiver().getUsername()
        );
    }
}

