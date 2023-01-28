package com.example.community.repository.report;

import com.example.community.domain.member.Member;
import com.example.community.domain.report.MemberReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberReportRepository extends JpaRepository<MemberReport,Long> {
    boolean existsByReporterAndReportedMember(Member member, Member reportedMember);
    List<MemberReport> findAllByReportedMember(Member reportedMember);
    void deleteAllByReportedMember(Member member);
}
