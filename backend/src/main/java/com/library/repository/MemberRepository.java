package com.library.repository;

import com.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// Provides database operations for members.
public interface MemberRepository extends JpaRepository<Member, Long> {

    // Checks whether the email address is already assigned to a member.
    boolean existsByEmail(String email);

    // Checks whether an email belongs to a member other than the supplied one.
    boolean existsByEmailAndIdNot(String email, Long id);
}
