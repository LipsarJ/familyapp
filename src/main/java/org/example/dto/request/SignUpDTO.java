package org.example.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpDTO(
        @NotBlank
        @Size(min = 3, max = 20)
        String username,

        @NotBlank
        @Size(min = 6, max = 20)
        String password,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String firstname,

        @NotBlank
        String middlename,

        @NotBlank
        String lastname
) {

}
