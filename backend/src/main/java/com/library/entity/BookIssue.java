package com.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

// Records a book loan, its member, and its return status.
@Entity
@Table(name = "book_issues")
public class BookIssue {

    // Uses a database-generated identifier for each issue.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Links the issue to the book without serializing an inverse collection.
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Links the issue to its member.
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Stores the date the book was issued.
    @Column(nullable = false)
    private LocalDate issueDate;

    // Remains empty until the book is returned.
    private LocalDate returnDate;

    // Stores ISSUED or RETURNED.
    @Column(nullable = false)
    private String status;

    // Creates an empty issue for JPA.
    public BookIssue() {
    }

    // Creates an issue with its related book, member, and dates.
    public BookIssue(Book book, Member member, LocalDate issueDate, LocalDate returnDate, String status) {
        this.book = book;
        this.member = member;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Returns the issue identifier.
    public Long getId() {
        return id;
    }

    // Sets the issue identifier.
    public void setId(Long id) {
        this.id = id;
    }

    // Returns the related book.
    public Book getBook() {
        return book;
    }

    // Updates the related book.
    public void setBook(Book book) {
        this.book = book;
    }

    // Returns the related member.
    public Member getMember() {
        return member;
    }

    // Updates the related member.
    public void setMember(Member member) {
        this.member = member;
    }

    // Returns the issue date.
    public LocalDate getIssueDate() {
        return issueDate;
    }

    // Updates the issue date.
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    // Returns the return date, or null when the book is still issued.
    public LocalDate getReturnDate() {
        return returnDate;
    }

    // Updates the return date.
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    // Returns the issue status.
    public String getStatus() {
        return status;
    }

    // Updates the issue status.
    public void setStatus(String status) {
        this.status = status;
    }
}
