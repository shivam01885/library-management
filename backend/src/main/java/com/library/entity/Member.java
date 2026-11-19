package com.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Stores a library member's contact details.
@Entity
@Table(name = "members")
public class Member {

    // Uses a database-generated identifier for each member.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the member's name.
    @Column(nullable = false)
    private String name;

    // Enforces one account per email address.
    @Column(nullable = false, unique = true)
    private String email;

    // Stores the member's 10-digit phone number.
    @Column(nullable = false, length = 10)
    private String phone;

    // Creates an empty member for JPA.
    public Member() {
    }

    // Creates a member from the supplied values.
    public Member(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Returns the database identifier.
    public Long getId() {
        return id;
    }

    // Sets the database identifier.
    public void setId(Long id) {
        this.id = id;
    }

    // Returns the member's name.
    public String getName() {
        return name;
    }

    // Updates the member's name.
    public void setName(String name) {
        this.name = name;
    }

    // Returns the member's email.
    public String getEmail() {
        return email;
    }

    // Updates the member's email.
    public void setEmail(String email) {
        this.email = email;
    }

    // Returns the member's phone number.
    public String getPhone() {
        return phone;
    }

    // Updates the member's phone number.
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
