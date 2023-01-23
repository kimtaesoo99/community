package com.example.community.dto.board;

import com.example.community.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardFindAllResponseDto {
    private Long id;
    private String title;
    private String nickname;

    public static BoardFindAllResponseDto toDto(Board board){
        return new BoardFindAllResponseDto(board.getId(), board.getTitle(), board.getMember().getUsername());
    }
}
