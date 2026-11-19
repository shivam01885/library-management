package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

// Provides database operations for books.
public interface BookRepository extends JpaRepository<Book, Long> {

    // Checks whether the ISBN is already assigned to another book.
    boolean existsByIsbn(String isbn);

    // Checks whether an ISBN belongs to a book other than the supplied one.
    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
