package com.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// Validates the member fields received from API clients.
public class MemberDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must contain exactly 10 digits")
    private String phone;

    // Creates an empty DTO for request binding.
    public MemberDto() {
    }

    // Creates a DTO from the supplied member fields.
    public MemberDto(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
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
