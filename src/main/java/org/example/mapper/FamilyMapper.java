package org.example.mapper;

import org.example.dto.response.ResponseFamilyDTO;
import org.example.entity.Family;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface FamilyMapper {
    @Mapping(target = "users", source = "family.users")
    ResponseFamilyDTO toResponseFamilyDTO(Family family);
}
