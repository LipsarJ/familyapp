package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.RequestUserDTO;
import org.example.dto.response.ResponseInvitationDTO;
import org.example.entity.Family;
import org.example.entity.Invitation;
import org.example.entity.InvitationStatus;
import org.example.entity.User;
import org.example.exception.extend.FamilyNotFoundException;
import org.example.exception.extend.UserNotFoundException;
import org.example.mapper.InvitationMapper;
import org.example.messaging.FamilyInvitationProducer;
import org.example.messaging.InvitationEvent;
import org.example.repo.FamilyRepo;
import org.example.repo.InvitationRepo;
import org.example.repo.UserRepo;
import org.example.sequrity.service.UserContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepo invitationRepo;
    private final UserRepo userRepo;
    private final FamilyRepo familyRepo;
    private final InvitationMapper invitationMapper;
    private final FamilyInvitationProducer familyInvitationProducer;
    private final UserContext userContext;

    /**
     * Получение списка активных приглашений для пользователя
     */
    public Page<ResponseInvitationDTO> getPendingInvitations(Pageable pageable) {
        User user = userRepo.findUserByUsername(userContext.getUserDto().getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        return invitationRepo.findByUserAndStatus(user, InvitationStatus.PENDING, pageable)
                .map(invitationMapper::toResponseInvitationDTO);
    }

    public void inviteUsersToFamily(Long familyId, RequestUserDTO requestUserDTO) {
        Family family = familyRepo.findById(familyId).orElseThrow(() -> new FamilyNotFoundException("Family not found"));
        User user = userRepo.findUserByUsername(requestUserDTO.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        InvitationEvent event = new InvitationEvent(
                user.getId(), family.getId(), family.getName(), user.getEmail()
        );
        familyInvitationProducer.sendInvitationEvent(event);

    }

    /**
     * Принятие приглашения пользователем
     */
    @Transactional
    public void acceptInvitation(Long invitationId) {
        User user = getUser();
        Invitation invitation = getInvitationForUser(invitationId, user);

        // Добавляем пользователя в семью
        Family family = invitation.getFamily();
        family.getUsers().add(user);
        familyRepo.save(family);

        // Помечаем приглашение как принятое
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepo.save(invitation);
    }

    /**
     * Отклонение приглашения
     */
    @Transactional
    public void rejectInvitation(Long invitationId) {
        User user = getUser();
        Invitation invitation = getInvitationForUser(invitationId, user);
        invitationRepo.delete(invitation);
    }

    /**
     * Создание нового приглашения
     */
    @Transactional
    public void createInvitation(Long userId, Long familyId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new IllegalArgumentException("Семья не найдена"));

        if (invitationRepo.existsByUserAndFamilyAndStatus(user, family, InvitationStatus.PENDING)) {
            throw new IllegalStateException("Приглашение уже существует");
        }

        Invitation invitation = new Invitation();
        invitation.setUser(user);
        invitation.setFamily(family);
        invitation.setStatus(InvitationStatus.PENDING);
        invitationRepo.save(invitation);
    }


    /**
     * Получение приглашения с проверкой, что оно принадлежит пользователю
     */
    private Invitation getInvitationForUser(Long invitationId, User user) {
        return invitationRepo.findByIdAndUser(invitationId, user)
                .orElseThrow(() -> new IllegalArgumentException("Приглашение не найдено или уже обработано"));
    }

    private User getUser() {
        return userRepo.findUserByUsername(userContext.getUserDto().getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
