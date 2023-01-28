package com.example.community.dto.admin;

import com.example.community.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportedBoardFindAllResponseDto {

    private Long id;
    private String title;
    private String nickname;


    public ReportedBoardFindAllResponseDto toDto(Board board) {
        return new ReportedBoardFindAllResponseDto (board.getId(), board.getTitle(), board.getMember().getUsername());
    }
}
