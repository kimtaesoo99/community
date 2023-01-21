package com.example.community.domain.member;

import com.example.community.domain.common.BaseEntity;
import com.example.community.domain.message.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "receiver",fetch = FetchType.LAZY)
    private List<Message> receivedMessages = new ArrayList<>();

    @OneToMany(mappedBy = "sender",fetch = FetchType.LAZY)
    private List<Message> sentMessages = new ArrayList<>();

    @Builder
    public Member(Long id, String username, String password, String name, Authority authority) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.authority = authority;
    }

    public void modify(String password,String name) {
        this.password = password;
        this.name = name;
        onPreUpdate();
    }

    public void sendMessage(Message message){
        sentMessages.add(message);
    }
    public void receiveMessage(Message message){
        receivedMessages.add(message);
    }
    public void deleteReceiveMessage(Message message){
        receivedMessages.remove(message);
    }
    public void deleteSendMessage(Message message){
        sentMessages.remove(message);
    }

}
