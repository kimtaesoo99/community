package com.example.community.factory;


import com.example.community.domain.comment.Comment;
import com.example.community.domain.member.Member;

import static com.example.community.factory.BoardFactory.createBoard;

public class CommentFactory {


    public static Comment createComment(Member member) {
        return new Comment(1L,"content", member, createBoard(),false);
    }
}
