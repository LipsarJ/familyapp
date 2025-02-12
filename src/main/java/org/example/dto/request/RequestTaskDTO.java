package org.example.dto.request;

import lombok.Data;
import org.example.entity.TaskStatus;

@Data
public class RequestTaskDTO {
    private String title;
    private String description;
    private TaskStatus status;
}
