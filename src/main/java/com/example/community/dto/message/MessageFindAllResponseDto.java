package com.example.community.dto.message;

import com.example.community.domain.message.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageFindAllResponseDto {
    private String title;
    private String senderName;
    private String receiverName;

    public static MessageFindAllResponseDto toDto(Message message) {
        return new MessageFindAllResponseDto(
            message.getTitle(),
            message.getSender().getUsername(),
            message.getReceiver().getUsername()
        );
    }
}
