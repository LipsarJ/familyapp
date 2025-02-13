package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.response.ResponseSharedProductDTO;
import org.example.entity.*;
import org.example.exception.extend.FamilyNotFoundException;
import org.example.exception.extend.ProductNotFoundException;
import org.example.exception.extend.UserNotFoundException;
import org.example.mapper.FamilyProductMapper;
import org.example.mapper.ProductMapper;
import org.example.mapper.UserProductMapper;
import org.example.repo.*;
import org.example.sequrity.service.UserContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final UserContext userContext;
    private final ProductRepo productRepo;
    private final FamilyRepo familyRepo;
    private final ProductMapper productMapper;
    private final UserRepo userRepo;
    private final FamilyProductRepo familyProductRepo;
    private final UserProductRepo userProductRepo;
    private final FamilyProductMapper familyProductMapper;
    private final UserProductMapper userProductMapper;

    public List<ResponseSharedProductDTO> getAllProductsForFamily(Long familyID) {
        Family family = familyRepo.findById(familyID).orElseThrow(() -> new FamilyNotFoundException("Family is not found"));
        List<ResponseSharedProductDTO> responseSharedProductDTOList = new ArrayList<>();
        family.getFamilyProducts().forEach(product -> {
            responseSharedProductDTOList.add(new ResponseSharedProductDTO(product.getProduct().getId(), product.getProduct().getName(), product.getStatus()));
        });

        return responseSharedProductDTOList;
    }

    public List<ResponseSharedProductDTO> getAllProductsForUser() {
        User user = userRepo.findUserByUsername(userContext.getUserDto().getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<ResponseSharedProductDTO> responseSharedProductDTOList = new ArrayList<>();
        user.getUserProducts().forEach(product -> {
            responseSharedProductDTOList.add(new ResponseSharedProductDTO(product.getProduct().getId(), product.getProduct().getName(), product.getStatus()));
        });
        return responseSharedProductDTOList;
    }


    @Transactional
    public ResponseSharedProductDTO addProductToFamily(Long familyID, Long productID) {
        Family family = familyRepo.findById(familyID).orElseThrow(() -> new FamilyNotFoundException("Family is not found"));
        Product product = productRepo.findById(productID).orElseThrow(() -> new ProductNotFoundException("Product is not found"));
        FamilyProduct familyProduct = new FamilyProduct();
        familyProduct.setFamily(family);
        familyProduct.setProduct(product);
        familyProduct.setStatus(ProductStatus.MIDDLE);
        familyProductRepo.save(familyProduct);
        return familyProductMapper.toResponseSharedProductDTO(familyProduct);
    }

    @Transactional
    public ResponseSharedProductDTO addProductToYourself(Long productID) {
        User user = userRepo.findUserByUsername(userContext.getUserDto().getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Product product = productRepo.findById(productID).orElseThrow(() -> new ProductNotFoundException("Product is not found"));
        UserProduct userProduct = new UserProduct();
        userProduct.setUser(user);
        userProduct.setProduct(product);
        userProduct.setStatus(ProductStatus.MIDDLE);
        userProductRepo.save(userProduct);
        return userProductMapper.toResponseSharedProductDTO(userProduct);
    }

}
