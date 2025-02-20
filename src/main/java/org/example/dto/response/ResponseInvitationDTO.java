package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.InvitationStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseInvitationDTO {
    private Long id;
    private ResponseUserDto user;
    private ResponseFamilyDTO family;
    private InvitationStatus status;
    private LocalDateTime createDate;
}
