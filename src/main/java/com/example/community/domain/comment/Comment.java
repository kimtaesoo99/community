package com.example.community.domain.comment;

import com.example.community.domain.board.Board;
import com.example.community.domain.common.BaseEntity;
import com.example.community.domain.member.Member;
import com.example.community.dto.comment.CommentEditRequestDto;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    private boolean reportedStatus;

    public Comment(String content, Member member, Board board) {
        this.content = content;
        this.member = member;
        this.board = board;
    }

    public boolean isOwnComment(Member member) {
        return this.member.equals(member);
    }

    public void edit(CommentEditRequestDto req){
        this.content = req.getContent();
    }

    public void reportComment(){
        reportedStatus = true;
    }

    public void unlockReportedStatus(){
        reportedStatus = false;
    }
}
