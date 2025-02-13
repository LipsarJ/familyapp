package org.example.repo;

import org.example.entity.FamilyProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyProductRepo extends JpaRepository<FamilyProduct, Long> {
}
