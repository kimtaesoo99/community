package com.example.community.dto.board;

import com.example.community.domain.board.Board;
import com.example.community.domain.board.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardFindResponseDto {

    private Long id;
    private String writer;
    private String title;
    private String content;
    private List<ImageDto> images;
    private LocalDateTime createAt;

    public static BoardFindResponseDto toDto(String writer,Board board) {
        return new BoardFindResponseDto(
            board.getId(),
            writer,
            board.getTitle(),
            board.getContent(),
            board.getImages().stream().map(i -> ImageDto.toDto(i)).collect(toList()),
            board.getCreatedAt()
        );
    }


}
