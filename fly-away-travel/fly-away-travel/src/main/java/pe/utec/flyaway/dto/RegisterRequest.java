package pe.utec.flyaway.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank @Pattern(regexp = ".*[A-Z].*", message = "firstName must contain at least one uppercase letter")
        String firstName,
        @NotBlank @Pattern(regexp = ".*[A-Z].*", message = "lastName must contain at least one uppercase letter")
        String lastName,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$", message = "password must have at least one letter and one number")
        String password
) {}
