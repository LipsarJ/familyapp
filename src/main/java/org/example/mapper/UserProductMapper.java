package org.example.mapper;

import org.example.dto.response.ResponseSharedProductDTO;
import org.example.entity.UserProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface UserProductMapper {
    
    @Mapping(target = "product", source = "userProduct.product")
    ResponseSharedProductDTO toResponseSharedProductDTO(UserProduct userProduct);
}
