package com.library.repository;

import com.library.entity.BookIssue;
import org.springframework.data.jpa.repository.JpaRepository;

// Provides database operations for book issues.
public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {
}
