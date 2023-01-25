package com.example.community.dto.comment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReadNumber {
    @NotNull(message = "게시글 번호를 입력해주세요.")
    @PositiveOrZero(message = "올바른 게시글 번호를 입력해주세요.")
    private Long boardId;
}
