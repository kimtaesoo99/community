package com.example.community.repository.member;

import com.example.community.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsernameAndPassword(String username, String password);
    Optional<Member> findByUsername(String username);
    public boolean existsByUsername(String username);

}
