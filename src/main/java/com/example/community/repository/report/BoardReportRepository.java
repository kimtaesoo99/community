package com.example.community.repository.report;

import com.example.community.domain.board.Board;
import com.example.community.domain.member.Member;
import com.example.community.domain.report.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {
    boolean existsByReporterAndReportedBoard(Member member, Board board);

    List<BoardReport> findAllByReportedBoard(Board board);
}
