package com.example.community.repository.member;

import com.example.community.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsernameAndPassword(String username, String password);
    Optional<Member> findByUsername(String username);
    public boolean existsByUsername(String username);

    @Query("select m from Member m where m.isReported =true")
    List<Member> findAllByReportedIsTrue();

}
