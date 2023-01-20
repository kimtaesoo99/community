package com.example.community.dto.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberEditRequestDto {


    @ApiModelProperty(value = "비밀번호", example = "123456")
    private String password;

    @ApiModelProperty(value = "사용자 이름", notes = "사용자 이름은 한글 또는 알파벳으로 입력해주세요.", required = true, example = "김태수")
    @Size(min = 2, message = "사용자 이름이 너무 짧습니다.")
    private String name;

}
