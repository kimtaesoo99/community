package com.example.community.service.comment;

import com.example.community.domain.board.Board;
import com.example.community.domain.comment.Comment;
import com.example.community.domain.member.Member;
import com.example.community.dto.comment.CommentCreateRequestDto;
import com.example.community.dto.comment.CommentEditRequestDto;
import com.example.community.dto.comment.CommentReadNumber;
import com.example.community.dto.comment.CommentResponseDto;
import com.example.community.exception.BoardNotFoundException;
import com.example.community.exception.CommentNotFoundException;
import com.example.community.exception.MemberNotEqualsException;
import com.example.community.repository.board.BoardRepository;
import com.example.community.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAllComments(CommentReadNumber number) {
        List<Comment> comments = commentRepository.findAllByBoardId(number.getBoardId());
        return comments.stream()
            .map(comment -> new CommentResponseDto().toDto(comment))
            .collect(Collectors.toList());
    }

    @Transactional
    public void createComment(CommentCreateRequestDto req, Member member) {
        Board board = boardRepository.findById(req.getBoardId()).orElseThrow(BoardNotFoundException::new);
        Comment comment = new Comment(req.getContent(), member, board);
        commentRepository.save(comment);
    }

    @Transactional
    public void editComment(CommentEditRequestDto req, Member member,Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        validateOwnComment(comment,member);
        comment.edit(req);
    }

    @Transactional
    public void deleteComment(Long id, Member member) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        validateOwnComment(comment, member);
        commentRepository.delete(comment);
    }

    private void validateOwnComment(Comment comment, Member member) {
        if (!comment.isOwnComment(member)) {
            throw new MemberNotEqualsException();
        }
    }
}
