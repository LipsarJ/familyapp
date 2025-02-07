package org.example.repo;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    Optional<User> findUserByUsername(String username);
}
