package com.example.community.domain.board;

import com.example.community.domain.member.Authority;
import com.example.community.domain.member.Member;
import com.example.community.dto.board.BoardUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.example.community.domain.board.Board.*;
import static com.example.community.factory.BoardFactory.createBoard;
import static com.example.community.factory.MemberFactory.createMember;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BoardTest {

    @Test
    public void 신고처리_테스트() {
        // given
        Board board = createBoard();
        board.reportBoard();

        // when
        boolean result = board.isReportedStatus();

        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void 좋아요수증가_테스트() {
        // given
        Board board = createBoard();

        // when
        board.increaseLikeCount();

        // then
        assertThat(board.getLikeCount()).isEqualTo(1);
    }

    @Test
    public void 좋아요수감소_테스트() {
        // given
        Board board = createBoard();

        // when
        board.increaseLikeCount();
        board.decreaseLikeCount();

        // then
        assertThat(board.getLikeCount()).isEqualTo(0);
    }

    @Test
    public void 조회수증가_테스트() {
        // given
        Board board = createBoard();

        // when
        board.increaseViewCount();

        // then
        assertThat(board.getViewCount()).isEqualTo(1);
    }


    @Test
    public void 신고해제_테스트() {
        // given
        Board board = createBoard();
        board.unlockReportedStatus();

        // when
        board.unlockReportedStatus();
        boolean result = board.isReportedStatus();

        // then
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void 게시판작성자확인_테스트() {
        //given
        Member own = new Member(1l,"username","1234","name", Authority.ROLE_USER);
        Board board = new Board("title", "content", own, null, List.of(new Image("a.png")));

        //when,then
        assertThat(board.isOwnBoard(own)).isTrue();
    }

    @Test
    public void 게시판작성자확인_다른사람접근_테스트() {
        //given
        Member own = new Member(1l,"username","1234","name", Authority.ROLE_USER);
        Member other = new Member(2l,"username2","12345","name2", Authority.ROLE_USER);
        Board board = new Board("title", "content", own, null, List.of(new Image("a.png")));

        //when,then
        assertThat(board.isOwnBoard(other)).isFalse();
    }

    @Test
    public void 게시판수정_테스트() {
        // given
        Image a = new Image(1l, "a", "origin_filename.jpg", null);
        Image b = new Image(2l, "b", "origin2_filename.jpg", null);

        List<Image> images = new ArrayList<>();
        images.add(a);
        images.add(b);

        Board board = builder()
            .title("title").content("content")
            .member(createMember())
            .images(images)
            .build();

        // when
        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "cFile".getBytes());
        BoardUpdateRequestDto req = new BoardUpdateRequestDto("upate title", "update content", List.of(cFile), List.of(a.getId()));
        ImageUpdatedResult imageUpdatedResult = board.update(req);

        // then
        //제목, 내용 변경 확인
        Assertions.assertThat(board.getTitle()).isEqualTo(req.getTitle());
        Assertions.assertThat(board.getContent()).isEqualTo(req.getContent());

        //이미지 추가, 기존 이미지 확인
        List<Image> resultImages = board.getImages();
        List<String> resultOriginNames = resultImages.stream().map(Image::getOriginName).collect(toList());
        Assertions.assertThat(resultImages.size()).isEqualTo(2);
        Assertions.assertThat(resultOriginNames).contains(b.getOriginName(), cFile.getOriginalFilename());


        //이미지 삭제 확인
        List<Image> deletedImages = imageUpdatedResult.getDeletedImages();
        List<String> deletedOriginNames = deletedImages.stream().map(Image::getOriginName).collect(toList());
        Assertions.assertThat(deletedImages.size()).isEqualTo(1);
        Assertions.assertThat(deletedOriginNames).contains(a.getOriginName());
    }
}
