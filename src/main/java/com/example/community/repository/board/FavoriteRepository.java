package com.example.community.repository.board;

import com.example.community.domain.board.Board;
import com.example.community.domain.board.Favorite;
import com.example.community.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    Optional<Favorite> findByBoardAndMember(Board board, Member member);

    Page<Favorite> findAllByMember(Member member, Pageable pageable);
}
