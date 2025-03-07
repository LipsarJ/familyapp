package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.RequestUserDTO;
import org.example.dto.response.PageDTO;
import org.example.dto.response.ResponseInvitationDTO;
import org.example.service.InvitationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("newAPI/family/invite")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;


    @GetMapping
    public PageDTO<ResponseInvitationDTO> getUserInvitations(Pageable pageable) {
        Page<ResponseInvitationDTO> invitations = invitationService.getPendingInvitations(pageable);
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
    public ResponseEntity<String> inviteUsersToFamily(@Valid @RequestBody RequestUserDTO requestUser, @PathVariable Long familyId) {
        invitationService.inviteUsersToFamily(familyId, requestUser);
        return ResponseEntity.ok("Приглашение отправлено!");
    }
}
