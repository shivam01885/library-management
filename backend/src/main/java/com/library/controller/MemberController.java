package com.library.controller;

import com.library.dto.MemberDto;
import com.library.entity.Member;
import com.library.service.MemberService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Exposes REST endpoints for member management.
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    // Receives the service that handles member rules.
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // Lists all members.
    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    // Returns one member by its identifier.
    @GetMapping("/{id}")
    public Member getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id);
    }

    // Creates a validated member and returns HTTP 201.
    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody MemberDto memberDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(memberDto));
    }

    // Updates a validated member.
    @PutMapping("/{id}")
    public Member updateMember(@PathVariable Long id, @Valid @RequestBody MemberDto memberDto) {
        return memberService.updateMember(id, memberDto);
    }

    // Deletes a member and returns HTTP 200.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok().build();
    }
}
