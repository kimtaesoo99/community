package com.example.community.domain.message;

import com.example.community.domain.member.Authority;
import com.example.community.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.community.factory.MemberFactory.createMember;
import static com.example.community.factory.MessageFactory.createMessage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MessageTest {

    Member sender;
    Member receiver;

    @BeforeEach
    public void beforeEach(){
        sender = createMember();
        receiver = new Member(2l, "username2", "1234", "name", Authority.ROLE_USER);
    }




    @Test
    public void 수신자가메시지삭제_테스트() {
        // given
        Message message = createMessage(1l,sender,receiver);

        // when
        message.deleteBySender();

        // then
        assertThat(message.isDeletedBySender()).isEqualTo(true);
    }

    @Test
    public void 발신자메시지삭제_테스트() {
        // given
        Message message = createMessage(1l,sender, receiver);

        // when
        message.deleteByReceiver();

        // then
        assertThat(message.isDeletedByReceiver()).isEqualTo(true);
    }

}
