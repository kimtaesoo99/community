package com.example.community.service.report;

import com.example.community.domain.board.Board;
import com.example.community.domain.comment.Comment;
import com.example.community.domain.member.Member;
import com.example.community.domain.report.BoardReport;
import com.example.community.domain.report.CommentReport;
import com.example.community.domain.report.MemberReport;
import com.example.community.dto.report.BoardReportRequestDto;
import com.example.community.dto.report.CommentReportRequestDto;
import com.example.community.dto.report.MemberReportRequestDto;
import com.example.community.exception.*;
import com.example.community.repository.board.BoardRepository;
import com.example.community.repository.comment.CommentRepository;
import com.example.community.repository.member.MemberRepository;
import com.example.community.repository.report.BoardReportRepository;
import com.example.community.repository.report.CommentReportRepository;
import com.example.community.repository.report.MemberReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private static final String SUCCESS_REPORT = "신고를 하였습니다.";

    private final BoardReportRepository boardReportRepository;
    private final MemberReportRepository memberReportRepository;
    private final CommentReportRepository commentReportRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public String reportBoard(Member member, BoardReportRequestDto req){
        Board reportedBoard = findReportedBoard(member, req.getReportedBoardId());
        BoardReport boardReport =
            new BoardReport(member, reportedBoard, req.getContent());
        boardReportRepository.save(boardReport);
        checkReportedBoard(reportedBoard);
        return SUCCESS_REPORT;
    }

    private Board findReportedBoard(Member member, Long reportedBoardId){
        Board board = boardRepository.findById(reportedBoardId).orElseThrow(BoardNotFoundException::new);
        if (board.getMember().equals(member)){
            throw new NotSelfReportException();
        }
        existReportedBoardHistory(member,board);
        return board;
    }

    private void existReportedBoardHistory(Member member, Board board){
       if (boardReportRepository.existsByReporterAndReportedBoard(member,board)){
           throw new AlreadyReportException();
       }
    }

    private void checkReportedBoard(Board reportedBoard){
        if (boardReportRepository.findAllByReportedBoard(reportedBoard).size()>5){
            reportedBoard.reportBoard();
        }
    }


    @Transactional
    public String reportMember(Member member, MemberReportRequestDto req){
        Member reportedMember = findReportedMember(member, req.getReportedMemberId());
        MemberReport memberReport =
            new MemberReport(member, reportedMember, req.getContent());
        memberReportRepository.save(memberReport);
        checkReportedMember(reportedMember);
        return SUCCESS_REPORT;
    }

    private Member findReportedMember(Member member, Long reportedMemberId){
        Member reportedMember = memberRepository.findById(reportedMemberId).orElseThrow(MemberNotFoundException::new);
        if (member.equals(reportedMember)){
            throw new NotSelfReportException();
        }
        existReportedMemberHistory(member,reportedMember);
        return reportedMember;
    }

    private void existReportedMemberHistory(Member member, Member reportedMember){
       if (memberReportRepository.existsByReporterAndReportedMember(member,reportedMember)){
           throw new AlreadyReportException();
       }
    }

    private void checkReportedMember(Member reportedMember){
        if (memberReportRepository.findAllByReportedMember(reportedMember).size()>5){
            reportedMember.reportMember();
        }
    }

    @Transactional
    public String reportComment(Member member, CommentReportRequestDto req){

        Comment reportedComment = findReportedComment(member, req.getReportedCommentId());

        CommentReport commentReport =
            new CommentReport(member, reportedComment, req.getContent());
        commentReportRepository.save(commentReport);
        checkReportedComment(reportedComment);
        return SUCCESS_REPORT;
    }

    private Comment findReportedComment(Member member, Long reportedCommentId){
        Comment comment = commentRepository.findById(reportedCommentId).orElseThrow(CommentNotFoundException::new);
        if (comment.getMember().equals(member)){
            throw new NotSelfReportException();
        }
        existReportedCommentHistory(member,comment);
        return comment;
    }

    private void existReportedCommentHistory(Member member, Comment comment){
        if (commentReportRepository.existsByReporterAndReportedComment(member,comment)){
            throw new AlreadyReportException();
        }
    }

    private void checkReportedComment(Comment reportedComment){
        if (commentReportRepository.findAllByReportedComment(reportedComment).size()>5){
            reportedComment.reportComment();
        }
    }

}
