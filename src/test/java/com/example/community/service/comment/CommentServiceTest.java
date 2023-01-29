package com.example.community.service.comment;

import com.example.community.domain.board.Board;
import com.example.community.domain.comment.Comment;
import com.example.community.domain.member.Member;
import com.example.community.dto.comment.CommentCreateRequestDto;
import com.example.community.dto.comment.CommentEditRequestDto;
import com.example.community.dto.comment.CommentReadNumber;
import com.example.community.dto.comment.CommentResponseDto;
import com.example.community.exception.MemberNotEqualsException;
import com.example.community.repository.board.BoardRepository;
import com.example.community.repository.comment.CommentRepository;
import com.example.community.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.community.factory.BoardFactory.createBoard;
import static com.example.community.factory.CommentFactory.createComment;
import static com.example.community.factory.ImageFactory.createImage;
import static com.example.community.factory.MemberFactory.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    BoardRepository boardRepository;


    @Test
    public void 댓글조회_테스트() {
        // given
        List<Comment> commentList = new ArrayList<>();
        commentList.add(createComment(createMember()));
        CommentReadNumber req = new CommentReadNumber(anyLong());
        given(commentRepository.findAllByBoardId(req.getBoardId())).willReturn(commentList);

        // when
        List<CommentResponseDto> result = commentService.findAllComments(req);

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void 댓글작성_테스트() {
        // given
        Board board = new Board(1L, "title", "content", createMember(),null, List.of(createImage()), 0, 0,false);
        CommentCreateRequestDto req = new CommentCreateRequestDto(board.getId(), "content");
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when
        commentService.createComment(req, createMember());

        // then
        verify(commentRepository).save(any());
    }

    @Test
    public void 댓글수정_테스트() {
        // given
        Member member = createMember();
        Comment comment = new Comment(1L, "content", member, createBoard(),false);
        CommentEditRequestDto req = new CommentEditRequestDto("newContent");
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when
        commentService.editComment(req, member, 1L);

        // then
        assertThat(comment.getContent()).isEqualTo("newContent");

    }

    @Test
    void 댓글삭제_테스트() {
        // given
        Member member = createMember();
        Comment comment = createComment(member);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when
        commentService.deleteComment(anyLong(), member);

        // then
        verify(commentRepository).delete(any());
    }

    @Test
    public void 댓글수정예외_테스트() {
        // given
        Member member = createMember();
        Member member2 = new Member(2l,"u","1","n",null);
        Comment comment = new Comment(1L, "content", member, createBoard(),false);
        CommentEditRequestDto req = new CommentEditRequestDto("newContent");
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when, then
        assertThatThrownBy(()->commentService.editComment(req, member2, 1L))
            .isInstanceOf(MemberNotEqualsException.class);
    }


    @Test
    void 댓글삭제예외_테스트() {
        // given
        Member member = createMember();
        Member member2 = new Member(2l,"u","1","n",null);
        Comment comment = createComment(member);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when, then
        assertThatThrownBy(()->commentService.deleteComment(anyLong(), member2))
            .isInstanceOf(MemberNotEqualsException.class);
    }


}
