package org.example.mapper;

import org.example.dto.response.ResponseInvitationDTO;
import org.example.entity.Invitation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface InvitationMapper {
    ResponseInvitationDTO toResponseInvitationDTO(Invitation invitation);
}
