package org.example.repo;

import org.example.entity.Family;
import org.example.entity.Invitation;
import org.example.entity.InvitationStatus;
import org.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepo extends JpaRepository<Invitation, Long> {

    /**
     * Получение списка активных (ожидающих) приглашений пользователя
     */
    Page<Invitation> findByUserAndStatus(User user, InvitationStatus status, Pageable pageable);

    /**
     * Проверка существования приглашения с указанным пользователем и семьёй
     */
    boolean existsByUserAndFamilyAndStatus(User user, Family family, InvitationStatus status);

    /**
     * Поиск приглашения по ID и пользователю
     */
    Optional<Invitation> findByIdAndUser(Long id, User user);
}
