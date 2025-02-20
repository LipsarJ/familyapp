package org.example.repo;

import org.example.entity.Family;
import org.example.entity.Task;
import org.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepo extends JpaRepository<Task, Long> {
    Page<Task> findAllByCreator(User user, Pageable pageable);

    @Query("SELECT t FROM Task t JOIN t.families f WHERE f = :family")
    Page<Task> findAllByFamily(Family family, Pageable pageable);

}
