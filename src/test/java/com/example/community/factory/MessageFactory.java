package com.example.community.factory;

import com.example.community.domain.member.Member;
import com.example.community.domain.message.Message;

public class MessageFactory {

    public static Message createMessage(Long id,Member sender, Member receiver) {
        return new Message(id,"title", "content", sender, receiver,false,false);
    }
}
