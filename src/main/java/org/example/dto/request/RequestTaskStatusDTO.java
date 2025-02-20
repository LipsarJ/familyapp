package org.example.dto.request;

import lombok.Data;
import org.example.entity.TaskStatus;

@Data
public class RequestTaskStatusDTO {
    private TaskStatus taskStatus;
}
