package com.library.service;

import com.library.dto.MemberDto;
import com.library.entity.Member;
import com.library.exception.DuplicateResourceException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Applies member rules and coordinates member database operations.
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    // Receives the repository needed for member operations.
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // Returns all members.
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // Finds one member or reports that it does not exist.
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member with ID " + id + " was not found"));
    }

    // Creates a member after checking that the email is unique.
    public Member createMember(MemberDto memberDto) {
        if (memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new DuplicateResourceException("A member with email " + memberDto.getEmail() + " already exists");
        }
        Member member = new Member(memberDto.getName(), memberDto.getEmail(), memberDto.getPhone());
        return memberRepository.save(member);
    }

    // Updates a member after checking that the email is unique.
    public Member updateMember(Long id, MemberDto memberDto) {
        Member member = getMemberById(id);
        if (memberRepository.existsByEmailAndIdNot(memberDto.getEmail(), id)) {
            throw new DuplicateResourceException("A member with email " + memberDto.getEmail() + " already exists");
        }
        member.setName(memberDto.getName());
        member.setEmail(memberDto.getEmail());
        member.setPhone(memberDto.getPhone());
        return memberRepository.save(member);
    }

    // Deletes a member unless another record still references them.
    @Transactional
    public void deleteMember(Long id) {
        Member member = getMemberById(id);
        memberRepository.delete(member);
    }
}
