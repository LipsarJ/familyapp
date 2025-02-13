package org.example.dto.request;

import lombok.Data;
import org.example.entity.ProductStatus;

@Data
public class RequestSharedProductDTO {
    private ProductStatus status;
}
