package com.example.community.repository.comment;

import com.example.community.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByBoardId(Long boarId);

    @Query("select c from Comment c where c.reportedStatus =true")
    List<Comment> findAllByReportedIsTrue();
}
