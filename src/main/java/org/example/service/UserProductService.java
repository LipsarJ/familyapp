package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.controlleradvice.Errors;
import org.example.dto.request.UpdateProductStatusDTO;
import org.example.dto.response.ResponseSharedProductDTO;
import org.example.entity.Product;
import org.example.entity.ProductStatus;
import org.example.entity.User;
import org.example.entity.UserProduct;
import org.example.exception.extend.IllegalStatusException;
import org.example.exception.extend.ProductNotFoundException;
import org.example.exception.extend.UserNotFoundException;
import org.example.mapper.UserProductMapper;
import org.example.repo.ProductRepo;
import org.example.repo.UserProductRepo;
import org.example.repo.UserRepo;
import org.example.repo.filter.FilterParam;
import org.example.repo.specification.ProductRelationSpecification;
import org.example.sequrity.service.UserContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProductService {

    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final UserProductRepo userProductRepo;
    private final UserContext userContext;
    private final UserProductMapper userProductMapper;

    public Page<ResponseSharedProductDTO> getAllProductsForUser(FilterParam filterParam, Pageable pageable) {
        User user = userRepo.findUserByUsername(userContext.getUserDto().getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Specification<UserProduct> spec = ProductRelationSpecification.filterByParams(filterParam, user);

        return userProductRepo.findAll(spec, pageable).map(userProductMapper::toResponseSharedProductDTO);
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

    @Transactional
    public ResponseSharedProductDTO updateProductStatusForUser(UpdateProductStatusDTO status, Long userProductId) {
        UserProduct userProduct = userProductRepo.findById(userProductId).orElseThrow(() -> new ProductNotFoundException("Product is not found"));
        try {
            userProduct.setStatus(ProductStatus.valueOf(status.getStatus()));
        } catch (IllegalArgumentException e) {
            throw new IllegalStatusException("Wrong status for product", Errors.ILLEGAL_STATUS);
        }
        userProductRepo.save(userProduct);
        return userProductMapper.toResponseSharedProductDTO(userProduct);
    }


    public void deleteProductFromUser(Long productId) {
        userProductRepo.deleteById(productId);
    }
}
