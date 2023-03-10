package com.example.community.service.report;

import com.example.community.domain.board.Board;
import com.example.community.domain.board.Image;
import com.example.community.domain.comment.Comment;
import com.example.community.domain.member.Member;
import com.example.community.domain.report.BoardReport;
import com.example.community.domain.report.CommentReport;
import com.example.community.domain.report.MemberReport;
import com.example.community.dto.report.BoardReportRequestDto;
import com.example.community.dto.report.CommentReportRequestDto;
import com.example.community.dto.report.MemberReportRequestDto;
import com.example.community.exception.NotSelfReportException;
import com.example.community.repository.board.BoardRepository;
import com.example.community.repository.comment.CommentRepository;
import com.example.community.repository.member.MemberRepository;
import com.example.community.repository.report.BoardReportRepository;
import com.example.community.repository.report.CommentReportRepository;
import com.example.community.repository.report.MemberReportRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.community.factory.BoardFactory.createBoardWithMember;
import static com.example.community.factory.CommentFactory.createComment;
import static com.example.community.factory.MemberFactory.createMember;
import static com.example.community.factory.MemberFactory.createMemberWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @InjectMocks
    ReportService reportService;

    @Mock
    BoardReportRepository boardReportRepository;
    @Mock
    MemberReportRepository memberReportRepository;
    @Mock
    CommentReportRepository commentReportRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    CommentRepository commentRepository;

    private static final String SUCCESS_REPORT = "????????? ???????????????.";


    @BeforeEach

    @Test
    public void ????????????_?????????() {
        // given
        Member reporter = createMember();
        Member reportedMember = createMemberWithId(3l);
        MemberReportRequestDto req = new MemberReportRequestDto(reportedMember.getId(), "???????????????.");
        MemberReport memberReportHistory = new MemberReport(1L, reporter, reportedMember,req.getContent());

        given(memberReportRepository.existsByReporterAndReportedMember(reporter,reportedMember)).willReturn(false);
        given(memberRepository.findById(req.getReportedMemberId())).willReturn(Optional.of(reportedMember));
        given(memberReportRepository.findAllByReportedMember(reportedMember)).willReturn(List.of(memberReportHistory));

        // when
        String result = reportService.reportMember(reporter, req);

        // then
        assertThat(result).isEqualTo(SUCCESS_REPORT);

    }


    @Test
    void ???????????????_?????????() {
        // given
        Member reporter = createMember();
        Member reportedMember = createMemberWithId(3l);
        Board reportedBoard = createBoardWithMember(reportedMember);
        BoardReportRequestDto req = new BoardReportRequestDto(reportedBoard.getId(), "???????????????.");
        BoardReport boardReport = new BoardReport(1L, reporter, reportedBoard, "content");

        given(boardRepository.findById(req.getReportedBoardId())).willReturn(Optional.of(reportedBoard));
        given(boardReportRepository.existsByReporterAndReportedBoard(reporter, reportedBoard)).willReturn(false);
        given(boardReportRepository.findAllByReportedBoard(reportedBoard)).willReturn(List.of(boardReport));

        // when
        String result = reportService.reportBoard(reporter, req);

        // then
        assertThat(result).isEqualTo(SUCCESS_REPORT);
    }

    @Test
    void ????????????_?????????() {
        // given
        Member reporter = createMember();
        Member reportedMember = createMemberWithId(3l);
        Comment reportedComment = createComment(reportedMember);
        CommentReportRequestDto req = new CommentReportRequestDto(reportedComment.getId(), "???????????????.");
        CommentReport commentReport = new CommentReport(1L, reporter, reportedComment, "content");

        given(commentRepository.findById(req.getReportedCommentId())).willReturn(Optional.of(reportedComment));
        given(commentReportRepository.existsByReporterAndReportedComment(reporter, reportedComment)).willReturn(false);
        given(commentReportRepository.findAllByReportedComment(reportedComment)).willReturn(List.of(commentReport));

        // when
        String result = reportService.reportComment(reporter, req);

        // then
        assertThat(result).isEqualTo(SUCCESS_REPORT);
    }

    @Test
    void ??????????????????????????????????????????(){
        //given
        Member member =  createMember();
        Board board = new Board(1l,"title","content",member,null,
            List.of(new Image("test.jpg")),0,0,false);
        BoardReportRequestDto req = new BoardReportRequestDto(board.getId(), "???????????????.");

        given(boardRepository.findById(req.getReportedBoardId())).willReturn(Optional.of(board));

        //when,then
        Assertions.assertThatThrownBy(() -> reportService.reportBoard(member,req))
            .isInstanceOf(NotSelfReportException.class);
    }

    @Test
    void ???????????????????????????????????????(){
        // given
        Member reportedMember = createMemberWithId(3l);
        Comment reportedComment = createComment(reportedMember);
        CommentReportRequestDto req = new CommentReportRequestDto(reportedComment.getId(), "???????????????.");
        CommentReport commentReport = new CommentReport(1L, reportedMember, reportedComment, "content");

        given(commentRepository.findById(req.getReportedCommentId())).willReturn(Optional.of(reportedComment));

        //when,then
        Assertions.assertThatThrownBy(() -> reportService.reportComment(reportedMember,req))
            .isInstanceOf(NotSelfReportException.class);
    }

    @Test
    void ????????????????????????(){
        Member reportedMember = createMemberWithId(3l);
        MemberReportRequestDto req = new MemberReportRequestDto(reportedMember.getId(), "???????????????.");

        given(memberRepository.findById(req.getReportedMemberId())).willReturn(Optional.of(reportedMember));

        //when,then
        Assertions.assertThatThrownBy(() -> reportService.reportMember(reportedMember,req))
            .isInstanceOf(NotSelfReportException.class);
    }
}
