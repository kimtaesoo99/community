package com.example.community.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiOperation(value = "댓글 수정 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEditRequestDto {
    @ApiModelProperty(value = "댓글", notes = "댓글을 입력해주세요.", required = true, example = "example comment")
    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;
}
