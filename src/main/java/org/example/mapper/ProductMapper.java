package org.example.mapper;

import org.example.dto.response.ResponseProductDTO;
import org.example.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface ProductMapper {
    ResponseProductDTO toResponseProductDTO(Product product);
}
