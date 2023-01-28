package com.example.community.dto.admin;

import com.example.community.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportedCommentFindAllResponseDto {
    private Long id;
    private String content;
    private String writer;


    public ReportedCommentFindAllResponseDto toDto(Comment comment) {
        return new ReportedCommentFindAllResponseDto(comment.getId(), comment.getContent(), comment.getMember().getUsername());
    }
}
