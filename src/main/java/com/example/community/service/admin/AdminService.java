package com.example.community.service.admin;

import com.example.community.domain.board.Board;
import com.example.community.domain.comment.Comment;
import com.example.community.domain.member.Member;
import com.example.community.dto.admin.ReportedBoardFindAllResponseDto;
import com.example.community.dto.admin.ReportedCommentFindAllResponseDto;
import com.example.community.dto.admin.ReportedMemberFindAllResponseDto;
import com.example.community.exception.BoardNotFoundException;
import com.example.community.exception.CommentNotFoundException;
import com.example.community.exception.MemberNotFoundException;
import com.example.community.exception.NotReportedException;
import com.example.community.repository.board.BoardRepository;
import com.example.community.repository.comment.CommentRepository;
import com.example.community.repository.member.MemberRepository;
import com.example.community.repository.report.BoardReportRepository;
import com.example.community.repository.report.CommentReportRepository;
import com.example.community.repository.report.MemberReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminService {

    private static final String UNLOCK ="신고가 해제되었습니다.";
    private static final String DELETE ="삭제가 되었습니다.";

    private final MemberRepository memberRepository;
    private final MemberReportRepository memberReportRepository;
    private final BoardRepository boardRepository;
    private final BoardReportRepository boardReportRepository;
    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;

    @Transactional(readOnly = true)
    public List<ReportedMemberFindAllResponseDto> findAllReportedMember() {
        List<Member> members = memberRepository.findAllByReportedIsTrue();
        return members.stream()
            .map(new ReportedMemberFindAllResponseDto()::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public String unlockMember(Long id){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        validateUnlockMember(member);
        unlockMember(member);
        return UNLOCK;
    }

    private void validateUnlockMember(Member member) {
        if (!member.isReportedStatus()) {
            throw new NotReportedException();
        }
    }

    private void unlockMember(Member member) {
        member.unLockedReportedStatus();
        memberReportRepository.deleteAllByReportedMember(member);
    }

    @Transactional
    public String deleteReportedMember(Long id){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        validateUnlockMember(member);
        memberRepository.delete(member);
        return DELETE;
    }

    @Transactional(readOnly = true)
    public List<ReportedBoardFindAllResponseDto> findAllReportedBoards() {
        List<Board> boards = boardRepository.findAllByReportedIsTrue();
        return boards.stream()
            .map(new ReportedBoardFindAllResponseDto()::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public String unlockBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateUnlockBoard(board);
        unlockBoard(board);
        return UNLOCK;
    }

    private void validateUnlockBoard(Board board) {
        if (!board.isReportedStatus()) {
            throw new NotReportedException();
        }
    }

    private void unlockBoard(Board board) {
        board.unlockReportedStatus();
        boardReportRepository.deleteAllByReportedBoard(board);
    }

    @Transactional
    public String deleteReportedBoard(Long id){
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateUnlockBoard(board);
        boardRepository.delete(board);
        return DELETE;
    }

    @Transactional(readOnly = true)
    public List<ReportedCommentFindAllResponseDto> findAllReportedComments() {
        List<Comment> comments = commentRepository.findAllByReportedIsTrue();
        return comments.stream()
            .map(new ReportedCommentFindAllResponseDto()::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public String unlockComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        validateUnlockComment(comment);
        unlockComment(comment);
        return UNLOCK;
    }

    private void validateUnlockComment(Comment comment) {
        if (!comment.isReportedStatus()) {
            throw new NotReportedException();
        }
    }

    private void unlockComment(Comment comment) {
        comment.unlockReportedStatus();
        commentReportRepository.deleteAllByReportedComment(comment);
    }

    @Transactional
    public String deleteReportedComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        validateUnlockComment(comment);
        commentRepository.delete(comment);
        return DELETE;
    }
}
