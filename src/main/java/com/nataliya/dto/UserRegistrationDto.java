package com.nataliya.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegistrationDto(
        @NotBlank(message = "Name should not be empty")
        @Pattern(
                regexp = "^[a-zA-Z0-9]{3,20}$",
                message = "Login must be 3 to 20 characters long and contain only letters and digits"
        )
        String login,
        @NotBlank(message = "Password should not be empty")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d).{6,20}$",
                message = "Password must be 6 to 20 characters long, contain at least one uppercase letter and one digit"
        )
        String password,
        @NotBlank(message = "Repeat password should not be empty")
        String repeatPassword
) {
    @AssertTrue(message = "Passwords do not match")
    private boolean isPasswordsMatch() {
        return password.equals(repeatPassword);
    }

}
