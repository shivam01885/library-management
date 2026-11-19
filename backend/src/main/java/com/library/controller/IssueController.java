package com.library.controller;

import com.library.dto.IssueRequestDto;
import com.library.entity.BookIssue;
import com.library.service.IssueService;
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

// Exposes REST endpoints for issuing and returning books.
@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    // Receives the service that handles issue rules.
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    // Lists all issues.
    @GetMapping
    public List<BookIssue> getAllIssues() {
        return issueService.getAllIssues();
    }

    // Returns one issue by its identifier.
    @GetMapping("/{id}")
    public BookIssue getIssueById(@PathVariable Long id) {
        return issueService.getIssueById(id);
    }

    // Creates an issue and returns HTTP 201.
    @PostMapping
    public ResponseEntity<BookIssue> createIssue(@Valid @RequestBody IssueRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(issueService.createIssue(request));
    }

    // Returns an issued book.
    @PutMapping("/{id}/return")
    public BookIssue returnIssue(@PathVariable Long id) {
        return issueService.returnIssue(id);
    }

    // Deletes an issue and returns HTTP 200.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        issueService.deleteIssue(id);
        return ResponseEntity.ok().build();
    }
}
