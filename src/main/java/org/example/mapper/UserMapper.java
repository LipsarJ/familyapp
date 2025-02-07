package org.example.mapper;

import org.example.dto.response.ResponseUserDto;
import org.example.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface UserMapper {
    ResponseUserDto toResponseUserDto( User user);
}
