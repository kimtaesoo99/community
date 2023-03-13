package com.example.community.repository.member;

import com.example.community.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("select m from Member m where m.reportedStatus =true")
    List<Member> findAllByReportedIsTrue();

}
