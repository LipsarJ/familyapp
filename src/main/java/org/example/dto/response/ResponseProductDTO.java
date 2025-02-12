package org.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseProductDTO {
    private Long id;
    private String name;
    private String status;
    private LocalDateTime updateDate;
}
