package com.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Stores a book and the number of copies currently available.
@Entity
@Table(name = "books")
public class Book {

    // Uses a database-generated identifier for each book.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the required display title.
    @Column(nullable = false)
    private String title;

    // Stores the author's name.
    @Column(nullable = false)
    private String author;

    // Keeps every book's ISBN unique.
    @Column(nullable = false, unique = true)
    private String isbn;

    // Tracks copies available for issue.
    @Column(nullable = false)
    private int quantity;

    // Creates an empty book for JPA.
    public Book() {
    }

    // Creates a book from the supplied values.
    public Book(String title, String author, String isbn, int quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
    }

    // Returns the database identifier.
    public Long getId() {
        return id;
    }

    // Sets the database identifier.
    public void setId(Long id) {
        this.id = id;
    }

    // Returns the book title.
    public String getTitle() {
        return title;
    }

    // Updates the book title.
    public void setTitle(String title) {
        this.title = title;
    }

    // Returns the author's name.
    public String getAuthor() {
        return author;
    }

    // Updates the author's name.
    public void setAuthor(String author) {
        this.author = author;
    }

    // Returns the ISBN.
    public String getIsbn() {
        return isbn;
    }

    // Updates the ISBN.
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    // Returns the number of available copies.
    public int getQuantity() {
        return quantity;
    }

    // Updates the number of available copies.
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
