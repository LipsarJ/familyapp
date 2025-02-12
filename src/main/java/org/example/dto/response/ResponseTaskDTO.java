package org.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseTaskDTO {
    private Long id;
    private String name;
    private String description;
    private String status;
    private ResponseUserDto creator;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
