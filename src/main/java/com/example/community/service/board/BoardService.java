package com.example.community.service.board;

import com.example.community.domain.board.Board;
import com.example.community.domain.board.Favorite;
import com.example.community.domain.board.Image;
import com.example.community.domain.board.Likes;
import com.example.community.domain.member.Member;
import com.example.community.dto.board.*;
import com.example.community.exception.*;
import com.example.community.repository.board.BoardRepository;
import com.example.community.repository.board.FavoriteRepository;
import com.example.community.repository.board.LikeRepository;
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

    private static final String LIKE_BOARD = "좋아요를 눌렀습니다.";
    private static final String LIKE_CANCER = "좋아요를 취소했습니다.";
    private static final String FAVORITE_BOARD = "즐겨찾기를 했습니다.";
    private static final String FAVORITE_CANCER = "즐겨찾기를 취소했습니다.";

    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final LikeRepository likeRepository;
    private final FavoriteRepository favoriteRepository;



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

    @Transactional
    public BoardFindResponseDto findBoard(Long id){
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        if (board.isReported())throw new BoardIsReportedStatusException();
        Member member = board.getMember();
        board.increaseViewCount();
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

    @Transactional
    public String updateLikeBoard(Long id, Member member){
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        if (!hasLikeBoard(board,member)){
            board.increaseLikeCount();
            return createLikeBoard(board, member);
        }
        board.decreaseLikeCount();
        return removeLikeBoard(board, member);
    }

    @Transactional
    public String updateFavoriteBoard(Long id, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        if (!hasFavoriteBoard(board, member)) {
            return createFavoriteBoard(board, member);
        }
        return removeFavoriteBoard(board, member);
    }

    @Transactional(readOnly = true)
    public BoardFindAllWithPagingResponseDto findFavoriteBoards(Integer page, Member member) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Favorite> favorites = favoriteRepository.findAllByMember(member,pageRequest);
        List<BoardFindAllResponseDto> boardsWithDto =  favorites.stream().map(Favorite::getBoard).map(BoardFindAllResponseDto::toDto)
            .collect(toList());
        return BoardFindAllWithPagingResponseDto.toDto(boardsWithDto, new PageInfoDto(favorites));
    }

    @Transactional(readOnly = true)
    public BoardFindAllWithPagingResponseDto findAllBoardsWithLikes(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("likeCount").descending());
        Page<Board> boards = boardRepository.findAll(pageRequest);
        List<BoardFindAllResponseDto> boardsWithDto = boards.stream().map(BoardFindAllResponseDto::toDto)
            .collect(toList());
        return BoardFindAllWithPagingResponseDto.toDto(boardsWithDto, new PageInfoDto(boards));
    }

    private void validateBoardWriter(Board board, Member member){
        if (!board.isOwnBoard(member)){
            throw new MemberNotEqualsException();
        }
    }

    private boolean hasLikeBoard(Board board, Member member){
        return likeRepository.findByBoardAndMember(board, member).isPresent();
    }

    private String createLikeBoard(Board board, Member member) {
        Likes likesBoard = new Likes(board, member);
        likeRepository.save(likesBoard);
        return LIKE_BOARD;
    }

    private String removeLikeBoard(Board board, Member member) {
        Likes likesBoard = likeRepository.findByBoardAndMember(board, member).orElseThrow(LikeNotFoundException::new);
        likeRepository.delete(likesBoard);
        return LIKE_CANCER;
    }

    private boolean hasFavoriteBoard(Board board, Member member) {
        return favoriteRepository.findByBoardAndMember(board, member).isPresent();
    }

    private String createFavoriteBoard(Board board, Member member) {
        Favorite favorite = new Favorite(board, member);
        favoriteRepository.save(favorite);
        return FAVORITE_BOARD;
    }

    private String removeFavoriteBoard(Board board, Member member) {
        Favorite favorite = favoriteRepository.findByBoardAndMember(board, member).orElseThrow(FavoriteNotFoundException::new);
        favoriteRepository.delete(favorite);
        return FAVORITE_CANCER;
    }


    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size())
            .forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

    private void deleteImages(List<Image> images) {
        images.forEach(i -> fileService.delete(i.getUniqueName()));
    }

}
