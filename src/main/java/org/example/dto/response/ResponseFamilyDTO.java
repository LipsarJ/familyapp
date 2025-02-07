package org.example.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class ResponseFamilyDTO {
    private String name;

    private List<ResponseUserDto> users;

    private OffsetDateTime updateDate;
}
