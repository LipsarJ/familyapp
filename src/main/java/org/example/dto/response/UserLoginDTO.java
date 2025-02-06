package org.example.dto.response;

public record UserLoginDTO(
        Long id,
        String username,
        String email
) {
}
