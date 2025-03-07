package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.ProductStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSharedProductDTO {
    private Long id;
    private ResponseProductDTO product;
    private ProductStatus status;
}
