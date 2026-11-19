package com.library.service;

import com.library.dto.BookDto;
import com.library.entity.Book;
import com.library.exception.DuplicateResourceException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Applies book rules and coordinates book database operations.
@Service
public class BookService {

    private final BookRepository bookRepository;

    // Receives the repository needed for book operations.
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Returns all books.
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Finds one book or reports that it does not exist.
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " was not found"));
    }

    // Creates a book after checking that its ISBN is unique.
    public Book createBook(BookDto bookDto) {
        if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new DuplicateResourceException("A book with ISBN " + bookDto.getIsbn() + " already exists");
        }
        Book book = new Book(bookDto.getTitle(), bookDto.getAuthor(), bookDto.getIsbn(), bookDto.getQuantity());
        return bookRepository.save(book);
    }

    // Updates a book after checking that its ISBN is unique.
    public Book updateBook(Long id, BookDto bookDto) {
        Book book = getBookById(id);
        if (bookRepository.existsByIsbnAndIdNot(bookDto.getIsbn(), id)) {
            throw new DuplicateResourceException("A book with ISBN " + bookDto.getIsbn() + " already exists");
        }
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbn(bookDto.getIsbn());
        book.setQuantity(bookDto.getQuantity());
        return bookRepository.save(book);
    }

    // Deletes a book unless another record still references it.
    @Transactional
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}
