package org.example.dto.response;

import lombok.*;

import java.time.OffsetDateTime;

@Data
public class ResponseUserDto {
    private Long id;

    private String username;

    private OffsetDateTime updateDate;
}
