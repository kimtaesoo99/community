package com.example.community.domain.report;

import com.example.community.domain.board.Board;
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
public class BoardReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id",nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board reportedBoard;

    @Column(nullable = false)
    private String content;

    public BoardReport(Member reporter, Board reportedBoard, String content) {
        this.reporter = reporter;
        this.reportedBoard = reportedBoard;
        this.content = content;
    }
}
