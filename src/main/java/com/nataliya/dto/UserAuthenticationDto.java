package com.nataliya.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserAuthenticationDto(

        @NotBlank(message = "Username should not be empty")
        String login,

        @NotBlank(message = "Password should not be empty")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d).{6,20}$",
                message = "Password must be 6 to 20 characters long, contain at least one uppercase letter and one digit"
        )
        String password
) {
}
