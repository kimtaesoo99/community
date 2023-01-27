package com.example.community.domain.report;

import com.example.community.domain.common.BaseEntity;
import com.example.community.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id",nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member reportedMember;

    @Column(nullable = false)
    private String content;


    public MemberReport(Member reporter, Member reportedMember, String content) {
        this.reporter = reporter;
        this.reportedMember = reportedMember;
        this.content = content;
    }
}
