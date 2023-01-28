package com.example.community.repository.report;


import com.example.community.domain.comment.Comment;
import com.example.community.domain.member.Member;
import com.example.community.domain.report.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentReportRepository extends JpaRepository<CommentReport,Long> {
    boolean existsByReporterAndReportedComment(Member member, Comment reportedComment);

    List<CommentReport> findAllByReportedComment(Comment reportedComment);
    void deleteAllByReportedComment(Comment comment);
}

