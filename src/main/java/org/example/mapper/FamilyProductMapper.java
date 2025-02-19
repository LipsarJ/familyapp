package org.example.mapper;

import org.example.dto.response.ResponseSharedProductDTO;
import org.example.entity.FamilyProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface FamilyProductMapper {

    @Mapping(target = "product", source = "familyProduct.product")
    ResponseSharedProductDTO toResponseSharedProductDTO(FamilyProduct familyProduct);
}
