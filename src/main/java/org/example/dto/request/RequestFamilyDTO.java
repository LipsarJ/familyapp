package org.example.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestFamilyDTO {
    private String name;
    private List<RequestUserDTO> users;
}
