package org.example.repo;

import org.example.entity.FamilyProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FamilyProductRepo extends JpaRepository<FamilyProduct, Long>, JpaSpecificationExecutor<FamilyProduct> {
}
