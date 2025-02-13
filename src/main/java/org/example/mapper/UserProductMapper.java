package org.example.mapper;

import org.example.dto.response.ResponseSharedProductDTO;
import org.example.entity.UserProduct;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface UserProductMapper {
    ResponseSharedProductDTO toResponseSharedProductDTO(UserProduct userProduct);
}
