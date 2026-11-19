package com.library.service;

import com.library.dto.IssueRequestDto;
import com.library.entity.Book;
import com.library.entity.BookIssue;
import com.library.entity.Member;
import com.library.exception.BookNotAvailableException;
import com.library.exception.DuplicateResourceException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookIssueRepository;
import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Applies issue and return rules while keeping book quantities consistent.
@Service
public class IssueService {

    private final BookIssueRepository issueRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    // Receives all repositories needed to manage an issue.
    public IssueService(
            BookIssueRepository issueRepository,
            BookRepository bookRepository,
            MemberRepository memberRepository) {
        this.issueRepository = issueRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    // Returns all recorded issues with their book and member details.
    public List<BookIssue> getAllIssues() {
        return issueRepository.findAll();
    }

    // Finds one issue or reports that it does not exist.
    public BookIssue getIssueById(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue with ID " + id + " was not found"));
    }

    // Creates an issue and reduces available book quantity in one transaction.
    @Transactional
    public BookIssue createIssue(IssueRequestDto request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book with ID " + request.getBookId() + " was not found"));
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member with ID " + request.getMemberId() + " was not found"));

        if (book.getQuantity() <= 0) {
            throw new BookNotAvailableException("No copies of \"" + book.getTitle() + "\" are available");
        }

        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        BookIssue issue = new BookIssue(book, member, LocalDate.now(), null, "ISSUED");
        return issueRepository.save(issue);
    }

    // Marks an issue returned and restores one available copy.
    @Transactional
    public BookIssue returnIssue(Long id) {
        BookIssue issue = getIssueById(id);
        if ("RETURNED".equals(issue.getStatus())) {
            throw new DuplicateResourceException("Issue with ID " + id + " has already been returned");
        }

        Book book = issue.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        issue.setReturnDate(LocalDate.now());
        issue.setStatus("RETURNED");
        return issueRepository.save(issue);
    }

    // Deletes an issue and restores its copy if the book had not yet been returned.
    @Transactional
    public void deleteIssue(Long id) {
        BookIssue issue = getIssueById(id);
        if ("ISSUED".equals(issue.getStatus())) {
            Book book = issue.getBook();
            book.setQuantity(book.getQuantity() + 1);
            bookRepository.save(book);
        }
        issueRepository.delete(issue);
    }
}
