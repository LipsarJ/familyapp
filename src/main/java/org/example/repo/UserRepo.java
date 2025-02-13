package org.example.repo;

import org.example.entity.Family;
import org.example.entity.Product;
import org.example.entity.Task;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    Optional<User> findUserByUsername(String username);

    @Query("select f from Family f join f.users u where u = :user")
    List<Family> findAllFamiliesByUser(User user);

    @Query("select t from Task t join t.creator c where c = :user")
    List<Task> findTasksByUser(User user);
}
