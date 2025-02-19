package org.example.repo.filter;

import jakarta.annotation.Nullable;
import lombok.Data;
import org.example.entity.ProductStatus;

@Data
public class FilterParam {
    @Nullable
    private String filterText;

    @Nullable
    private ProductStatus status;
}
