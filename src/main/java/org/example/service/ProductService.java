package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.response.ResponseProductDTO;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.exception.extend.UserNotFoundException;
import org.example.mapper.ProductMapper;
import org.example.repo.ProductRepo;
import org.example.repo.UserRepo;
import org.example.sequrity.service.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final UserContext userContext;
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final UserRepo userRepo;

    public List<ResponseProductDTO> getAllProductsForUser() {
        User user = userRepo.findUserByUsername(userContext.getUserDto().getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Product> products = userRepo.findProductsByUser(user);
        return products.stream().map(productMapper::toResponseProductDTO).collect(Collectors.toList());
    }

}
