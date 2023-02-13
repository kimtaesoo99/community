package com.example.community.domain.comment;

import com.example.community.domain.member.Authority;
import com.example.community.domain.member.Member;
import com.example.community.dto.comment.CommentEditRequestDto;
import org.junit.jupiter.api.Test;

import static com.example.community.factory.CommentFactory.createComment;
import static com.example.community.factory.MemberFactory.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommentTest {

    @Test
    public void 본인댓글접근_테스트() {
        // given
        Member member = createMember();
        Comment comment = createComment(member);

        // when
        boolean result = comment.isOwnComment(member);

        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void 다른댓글접근_테스트() {
        // given
        Member member = createMember();
        Member other = new Member(2l,"u","1234","name", Authority.ROLE_USER);
        Comment comment = createComment(other);

        // when
        boolean result = comment.isOwnComment(member);

        // then
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void 댓글수정_테스트(){
        //given
        Comment comment = createComment(createMember());
        CommentEditRequestDto req = new CommentEditRequestDto("update content");

        //when
        comment.edit(req);

        //then
        assertThat(comment.getContent()).isEqualTo("update content");
    }

    @Test
    public void 댓글신고_테스트(){
        //given
        Comment comment = createComment(createMember());

        //when
        comment.reportComment();

        //then
        assertThat(comment.isReportedStatus()).isTrue();
    }

    @Test
    public void 댓글신고해제_테스트(){
        //given
        Comment comment = createComment(createMember());

        //when
        comment.reportComment();
        comment.unlockReportedStatus();

        //then
        assertThat(comment.isReportedStatus()).isFalse();
    }


}
