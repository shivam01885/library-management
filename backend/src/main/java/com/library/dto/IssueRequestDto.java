package com.library.dto;

import jakarta.validation.constraints.NotNull;

// Validates the book and member identifiers for a new issue.
public class IssueRequestDto {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Member ID is required")
    private Long memberId;

    // Creates an empty DTO for request binding.
    public IssueRequestDto() {
    }

    // Creates a DTO from the selected book and member identifiers.
    public IssueRequestDto(Long bookId, Long memberId) {
        this.bookId = bookId;
        this.memberId = memberId;
    }

    // Returns the selected book identifier.
    public Long getBookId() {
        return bookId;
    }

    // Updates the selected book identifier.
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    // Returns the selected member identifier.
    public Long getMemberId() {
        return memberId;
    }

    // Updates the selected member identifier.
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
