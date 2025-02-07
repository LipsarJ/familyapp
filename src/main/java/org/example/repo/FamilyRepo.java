package org.example.repo;

import org.example.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamilyRepo extends JpaRepository<Family, Long> {

    Optional<Family> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
