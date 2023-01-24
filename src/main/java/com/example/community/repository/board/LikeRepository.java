package com.example.community.repository.board;

import com.example.community.domain.board.Board;
import com.example.community.domain.board.Likes;
import com.example.community.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByBoardAndMember(Board board, Member member);
}
