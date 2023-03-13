package com.example.community.service.board;

import com.example.community.domain.board.Board;
import com.example.community.domain.board.Favorite;
import com.example.community.domain.board.Image;
import com.example.community.domain.board.Likes;
import com.example.community.domain.member.Member;
import com.example.community.dto.board.BoardCreateRequestDto;
import com.example.community.dto.board.BoardFindAllWithPagingResponseDto;
import com.example.community.dto.board.BoardFindResponseDto;
import com.example.community.dto.board.BoardUpdateRequestDto;
import com.example.community.repository.board.BoardRepository;
import com.example.community.repository.board.FavoriteRepository;
import com.example.community.repository.board.LikeRepository;
import com.example.community.service.file.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.community.factory.BoardFactory.createBoard;
import static com.example.community.factory.ImageFactory.createImage;
import static com.example.community.factory.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @InjectMocks
    BoardService boardService;
    @Mock
    BoardRepository boardRepository;
    @Mock
    FileService fileService;
    @Mock
    LikeRepository likeRepository;
    @Mock
    FavoriteRepository favoriteRepository;

    private static final String SUCCESS_LIKE = "좋아요를 눌렀습니다.";
    private static final String CANCER_LIKE = "좋아요를 취소했습니다.";
    private static final String SUCCESS_FAVORITE = "즐겨찾기를 했습니다.";
    private static final String CANCER_FAVORITE = "즐겨찾기를 취소했습니다.";

    @Test
    public void 게시판생성_테스트() throws Exception{
        //given
        BoardCreateRequestDto req = new BoardCreateRequestDto("title", "content", List.of(
            new MockMultipartFile("test", "test.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes())
        ));

        Member member = createMember();

        //when
        boardService.createBoard(req,member,null);

        //then
        verify(boardRepository).save(any());
    }

    @Test
    public void 게시글목록조회_테스트() throws Exception{
        //given
        Integer page = 1;
        Long categoryId = 1l;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        List<Board> boards = new ArrayList<>();
        boards.add(createBoard());
        Page<Board> boardsWithPaging = new PageImpl<>(boards);
        given(boardRepository.findAllByCategoryId(categoryId,pageRequest)).willReturn(boardsWithPaging);

        //when
        BoardFindAllWithPagingResponseDto result = boardService.findAllBoardsWithCategory(page,categoryId);

        //then
        assertThat(result.getBoards().size()).isEqualTo(1);
    }

    @Test
    public void 게시글전체조회_테스트() throws Exception{
        //given
        Integer page = 1;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        List<Board> boards = new ArrayList<>();
        boards.add(createBoard());
        Page<Board> boardsWithPaging = new PageImpl<>(boards);
        given(boardRepository.findAll(pageRequest)).willReturn(boardsWithPaging);

        //when
        BoardFindAllWithPagingResponseDto result = boardService.findAllBoards(page);

        //then
        assertThat(result.getBoards().size()).isEqualTo(1);
    }

    @Test
    public void 게시글단건조회_테스트() throws Exception{
        //given
        Long id = 1L;
        Board board = createBoard();
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        //when
        BoardFindResponseDto result = boardService.findBoard(id);

        //then
        assertThat(result.getTitle()).isEqualTo("title");
    }

    @Test
    public void 게시글수정_테스트() throws Exception{
        //given
        Long id = 1l;
        BoardUpdateRequestDto req = new BoardUpdateRequestDto("title","content",List.of(
            new MockMultipartFile("test", "test.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes())
        ),List.of());
        Member member = createMember();
        Board board = new Board("t","c",member,null,List.of(new Image("a.png")));
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        //when
        boardService.editBoard(id,req,member);

        //then
        assertThat(board.getTitle()).isEqualTo("title");
    }

    @Test
    public void 게시글삭제_테스트() throws Exception{
        //given
        Long id = 1l;
        Board board = createBoard();
        Member member = board.getMember();
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        //when
        boardService.deleteBoard(id,member);

        //then
        verify(boardRepository).delete(any());
    }

    @Test
    public void 게시글검색_테스트() throws Exception{
        //given
        String keyword = "title";
        Integer page = 1;

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        List<Board> boards = new ArrayList<>();
        boards.add(createBoard());
        Page<Board> boardsWithPaging = new PageImpl<>(boards);
        given(boardRepository.findAllByTitleContaining(keyword,pageRequest)).willReturn(boardsWithPaging);

        //when
        BoardFindAllWithPagingResponseDto result = boardService.searchBoard(keyword,page);

        //then
        assertThat(result.getBoards().size()).isEqualTo(1);
    }

    @Test
    public void 게시판좋아요추가_테스트() throws Exception{
        //given
        Long id = 1L;
        Member member = createMember();
        Board board = createBoard();

        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        //when
        String result = boardService.updateLikeBoard(id, member);

        // then
        assertThat(result).isEqualTo(SUCCESS_LIKE);
        verify(likeRepository).save(any());
    }

    @Test
    public void 게시판좋아요취소_테스트() throws Exception{
        //given
        Long id = 1L;
        Member member = createMember();
        Board board = createBoard();
        Likes likes = new Likes(board, member);

        given(boardRepository.findById(id)).willReturn(Optional.of(board));
        given(likeRepository.findByBoardAndMember(board, member)).willReturn(Optional.of(likes));

        //when
        String result = boardService.updateLikeBoard(id, member);

        // then
        assertThat(result).isEqualTo(CANCER_LIKE);
        verify(likeRepository).delete(any());
    }

    @Test
    public void 게시판즐겨찾기추가_테스트() throws Exception{
        //given
        Long id = 1L;
        Member member = createMember();
        Board board = createBoard();

        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        //when
        String result = boardService.updateFavoriteBoard(id, member);

        // then
        assertThat(result).isEqualTo(SUCCESS_FAVORITE);
        verify(favoriteRepository).save(any());
    }

    @Test
    public void 게시판즐겨찾기취소_테스트() throws Exception{
        //given
        Long id = 1L;
        Member member = createMember();
        Board board = createBoard();
        Favorite favorite = new Favorite(board, member);

        given(boardRepository.findById(id)).willReturn(Optional.of(board));
        given(favoriteRepository.findByBoardAndMember(board, member)).willReturn(Optional.of(favorite));

        //when
        String result = boardService.updateFavoriteBoard(id, member);

        // then
        assertThat(result).isEqualTo(CANCER_FAVORITE);
        verify(favoriteRepository).delete(any());
    }


    @Test
    public void 게시글즐겨찾기목록조회_테스트() throws Exception{
        //given
        Integer page = 0;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        Member member = createMember();
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(new Favorite(1L,createBoard(),member));
        Page<Favorite> favoritesWithPaging = new PageImpl<>(favorites);
        given(favoriteRepository.findAllByMember(member,pageRequest)).willReturn(favoritesWithPaging);

        //when
        BoardFindAllWithPagingResponseDto result = boardService.findFavoriteBoards(page,member);

        //then
        assertThat(result.getBoards().size()).isEqualTo(1);
    }

    @Test
    public void 게시글인기순조회_테스트() throws Exception{
        //given
        Integer page = 0;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("likeCount").descending());
        List<Board> boards = new ArrayList<>();
        Board board = new Board("likeBoard", "content", createMember(),null,List.of(createImage()));
        board.increaseLikeCount();
        boards.add(board);
        boards.add(createBoard());

        Page<Board> boardsWithPaging = new PageImpl<>(boards);
        given(boardRepository.findAll(pageRequest)).willReturn(boardsWithPaging);

        //when
        BoardFindAllWithPagingResponseDto result = boardService.findAllBoardsWithLikes(page);

        //then
        assertThat(result.getBoards().get(0).getTitle()).isEqualTo("likeBoard");
    }

}
