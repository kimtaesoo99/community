package com.example.community.domain.member;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.example.community.factory.MemberFactory.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MemberTest {

    @Test
    public void 신고_테스트() {
        // given
        Member member = createMember();

        // when
        member.reportMember();

        // then
        assertThat(member.isReportedStatus()).isTrue();
        assertThat(member.getAuthority()).isEqualTo(Authority.ROLE_SUSPENSION);
    }

    @Test
    public void 신고해제_테스트() {
        // given
        Member member = createMember();
        member.reportMember();

        // when
        member.unLockedReportedStatus();

        // then
        assertThat(member.isReportedStatus()).isFalse();
        assertThat(member.getAuthority()).isEqualTo(Authority.ROLE_USER);
    }

    @Test
    public void 유저수정_테스트() {
        // given
        Member member = createMember();
        UUID uuid = UUID.randomUUID();

        // when
        member.modify(String.valueOf(uuid),"new name");

        // then
        assertThat(member.getName()).isEqualTo("new name");
        assertThat(member.getPassword()).isEqualTo(String.valueOf(uuid));
    }
}
