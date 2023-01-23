package com.example.community.service.board;

import com.example.community.domain.board.Board;
import com.example.community.domain.board.Image;
import com.example.community.domain.member.Member;
import com.example.community.dto.board.*;
import com.example.community.exception.BoardNotFoundException;
import com.example.community.exception.MemberNotEqualsException;
import com.example.community.repository.board.BoardRepository;
import com.example.community.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileService fileService;

    @Transactional
    public void createBoard(BoardCreateRequestDto req, Member member){
        List<Image> images = req.getImages().stream()
            .map(i -> new Image(i.getOriginalFilename()))
            .collect(toList());
        Board board = new Board(req.getTitle(), req.getContent(), member, images);
        boardRepository.save(board);
        uploadImages(board.getImages(), req.getImages());
    }

    @Transactional(readOnly = true)
    public BoardFindAllWithPagingResponseDto findAllBoards(Integer page){
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Board> boards = boardRepository.findAll(pageRequest);
        List<BoardFindAllResponseDto> boardsWithDto = boards.stream().map(BoardFindAllResponseDto::toDto)
            .collect(toList());
        return BoardFindAllWithPagingResponseDto.toDto(boardsWithDto, new PageInfoDto(boards));
    }

    @Transactional(readOnly = true)
    public BoardFindResponseDto findBoard(Long id){
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        Member member = board.getMember();
        return BoardFindResponseDto.toDto(member.getUsername(), board);
    }

    @Transactional
    public void editBoard(Long id, BoardUpdateRequestDto req, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateBoardWriter(board,member);
        Board.ImageUpdatedResult result = board.update(req);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());
    }

    @Transactional
    public void deleteBoard(Long id, Member member){
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateBoardWriter(board, member);
        deleteImages(board.getImages());
        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public BoardFindAllWithPagingResponseDto searchBoard(String keyword,Integer page){
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Board> boards = boardRepository.findAllByTitleContaining(keyword,pageRequest);
        List<BoardFindAllResponseDto> boardsWithDto = boards.stream().map(BoardFindAllResponseDto::toDto)
            .collect(toList());
        return BoardFindAllWithPagingResponseDto.toDto(boardsWithDto, new PageInfoDto(boards));
    }

    private void validateBoardWriter(Board board, Member member){
        if (!member.equals(board.getMember())){
            throw new MemberNotEqualsException();
        }
    }


    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size())
            .forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

    private void deleteImages(List<Image> images) {
        images.forEach(i -> fileService.delete(i.getUniqueName()));
    }
}
