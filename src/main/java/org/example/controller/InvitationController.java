package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.RequestFamilyDTO;
import org.example.dto.response.PageDTO;
import org.example.dto.response.ResponseInvitationDTO;
import org.example.entity.User;
import org.example.service.InvitationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("newAPI/family/invite")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;


    @GetMapping
    public PageDTO<ResponseInvitationDTO> getUserInvitations(@AuthenticationPrincipal User user, Pageable pageable) {
        Page<ResponseInvitationDTO> invitations = invitationService.getPendingInvitations(user, pageable);
        return new PageDTO<>(
                invitations.getContent(),
                invitations.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<String> acceptInvitation(@PathVariable Long id) {
        invitationService.acceptInvitation(id);
        return ResponseEntity.ok("Вы приняли приглашение");
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectInvitation(@PathVariable Long id) {
        invitationService.rejectInvitation(id);
        return ResponseEntity.ok("Вы отклонили приглашение");
    }


    @PutMapping("{familyId}")
    public ResponseEntity<String> inviteUsersToFamily(@Valid @RequestBody RequestFamilyDTO familyDTO) {
        invitationService.inviteUsersToFamily(familyDTO);
        return ResponseEntity.ok("Приглашения отправлены!");
    }
}
