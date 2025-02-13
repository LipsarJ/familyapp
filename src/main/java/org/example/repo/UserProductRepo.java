package org.example.repo;

import org.example.entity.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProductRepo extends JpaRepository<UserProduct, Long> {
}
