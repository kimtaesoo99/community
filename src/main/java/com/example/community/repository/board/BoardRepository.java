package com.example.community.repository.board;


import com.example.community.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board,Long> {

    @Query(value = "select b from Board b join fetch b.member m"
        , countQuery = "select count(b) from Board b order by b.id desc ")
    Page<Board> findAll(Pageable pageable);


    @Query(value = "select b from Board b join fetch b.member m where b.title like '%title%'"
        , countQuery = "select count(b) from Board b order by b.id desc")
    Page<Board> findAllByTitleContaining(@Param("title") String keyword, Pageable pageable);

    @Query("select b from Board b where b.reportedStatus =true")
    List<Board> findAllByReportedIsTrue();

    Page<Board> findAllByCategoryId(Long categoryId, Pageable pageable);
}




