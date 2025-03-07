package org.example.repo;

import org.example.entity.Product;
import org.example.entity.User;
import org.example.entity.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserProductRepo extends JpaRepository<UserProduct, Long>, JpaSpecificationExecutor<UserProduct> {
    boolean existsByUserAndProduct(User user, Product product);
}
