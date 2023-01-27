package com.example.community.domain.report;

import com.example.community.domain.comment.Comment;
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
public class CommentReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id",nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment reportedComment;

    @Column(nullable = false)
    private String content;

    public CommentReport(Member reporter, Comment reportedComment, String content) {
        this.reporter = reporter;
        this.reportedComment = reportedComment;
        this.content = content;
    }
}
