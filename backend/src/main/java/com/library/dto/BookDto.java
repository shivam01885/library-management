package com.library.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

// Validates the book fields received from API clients.
public class BookDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @Min(value = 0, message = "Quantity must be zero or greater")
    private int quantity;

    // Creates an empty DTO for request binding.
    public BookDto() {
    }

    // Creates a DTO from the supplied book fields.
    public BookDto(String title, String author, String isbn, int quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
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

    // Returns the available quantity.
    public int getQuantity() {
        return quantity;
    }

    // Updates the available quantity.
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
