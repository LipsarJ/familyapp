package org.example.mapper;

import org.example.dto.response.ResponseSharedProductDTO;
import org.example.entity.FamilyProduct;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface FamilyProductMapper {
    ResponseSharedProductDTO toResponseSharedProductDTO(FamilyProduct familyProduct);
}
