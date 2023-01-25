package com.example.community.dto.comment;


import com.example.community.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    public CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
            comment.getId(),
            comment.getContent(),
            comment.getMember().getUsername(),
            comment.getCreatedAt(),
            comment.getModifiedAt()
        );
    }
}
