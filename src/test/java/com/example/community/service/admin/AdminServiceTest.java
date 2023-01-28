package com.example.community.service.admin;

import com.example.community.domain.board.Board;
import com.example.community.domain.comment.Comment;
import com.example.community.domain.member.Member;
import com.example.community.dto.admin.ReportedBoardFindAllResponseDto;
import com.example.community.dto.admin.ReportedCommentFindAllResponseDto;
import com.example.community.dto.admin.ReportedMemberFindAllResponseDto;
import com.example.community.repository.board.BoardRepository;
import com.example.community.repository.comment.CommentRepository;
import com.example.community.repository.member.MemberRepository;
import com.example.community.repository.report.BoardReportRepository;
import com.example.community.repository.report.CommentReportRepository;
import com.example.community.repository.report.MemberReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.community.factory.BoardFactory.createBoard;
import static com.example.community.factory.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @InjectMocks
    AdminService adminService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    MemberReportRepository memberReportRepository;
    @Mock
    BoardReportRepository boardReportRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    CommentReportRepository commentReportRepository;

    @Test
    public void 신고된유저_목록조회_테스트() {
        // given
        List<Member> members = new ArrayList<>();
        members.add(createMember());
        given(memberRepository.findAllByReportedIsTrue()).willReturn(members);

        // when
        List<ReportedMemberFindAllResponseDto> result = adminService.findAllReportedMember();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void 신고된유저_정지해제_테스트() {
        // given
        Member member = createMember();
        member.isReportedStatus();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        String result = adminService.unlockMember(anyLong());

        // then
        assertThat(result).isEqualTo("신고가 해제되었습니다.");
        assertThat(member.isReported()).isFalse();
        verify(memberReportRepository).deleteAllByReportedMember(member);
    }

    @Test
    public void 신고된유저_삭제_테스트(){
        //given
        Member member = createMember();
        member.isReportedStatus();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        String result = adminService.deleteReportedMember(anyLong());

        // then
        assertThat(result).isEqualTo("삭제가 되었습니다.");
        verify(memberRepository).delete(any());
    }

    @Test
    void 신고된게시글_목록조회_테스트() {
        // given
        List<Board> boards = new ArrayList<>();
        boards.add(createBoard());
        given(boardRepository.findAllByReportedIsTrue()).willReturn(boards);

        // when
        List<ReportedBoardFindAllResponseDto> result = adminService.findAllReportedBoards();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void 신고된게시글_정지해제_테스트() {
        // given
        Board board = createBoard();
        board.isReportedStatus();
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when
        String result = adminService.unlockBoard(anyLong());

        // then
        assertThat(result).isEqualTo("신고가 해제되었습니다.");
        assertThat(board.isReported()).isFalse();
        verify(boardReportRepository).deleteAllByReportedBoard(board);
    }

    @Test
    public void 신고된게시글_삭제_테스트(){
        //given
        Board board = createBoard();
        board.isReportedStatus();
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        String result = adminService.deleteReportedBoard(anyLong());

        // then
        assertThat(result).isEqualTo("삭제가 되었습니다.");
        verify(boardRepository).delete(any());
    }

    @Test
    void 신고된댓글_목록조회_테스트() {
        // given
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("content",createMember(),createBoard()));
        given(commentRepository.findAllByReportedIsTrue()).willReturn(comments);

        // when
        List<ReportedCommentFindAllResponseDto> result = adminService.findAllReportedComments();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void 신고된댓글_정지해제_테스트() {
        // given
        Comment comment = new Comment("content", createMember(), createBoard());
        comment.isReportedStatus();
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when
        String result = adminService.unlockComment(anyLong());

        // then
        assertThat(result).isEqualTo("신고가 해제되었습니다.");
        assertThat(comment.isReported()).isFalse();
        verify(commentReportRepository).deleteAllByReportedComment(comment);
    }

    @Test
    public void 신고된댓글_삭제_테스트(){
        //given
        Comment comment = new Comment("content", createMember(), createBoard());
        comment.isReportedStatus();
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        String result = adminService.deleteReportedComment(anyLong());

        // then
        assertThat(result).isEqualTo("삭제가 되었습니다.");
        verify(commentRepository).delete(any());
    }
}
