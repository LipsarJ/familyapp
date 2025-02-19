package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.controlleradvice.Errors;
import org.example.dto.request.UpdateProductStatusDTO;
import org.example.dto.response.ResponseSharedProductDTO;
import org.example.entity.Family;
import org.example.entity.FamilyProduct;
import org.example.entity.Product;
import org.example.entity.ProductStatus;
import org.example.exception.extend.FamilyNotFoundException;
import org.example.exception.extend.IllegalStatusException;
import org.example.exception.extend.ProductNotFoundException;
import org.example.mapper.FamilyProductMapper;
import org.example.repo.FamilyProductRepo;
import org.example.repo.FamilyRepo;
import org.example.repo.ProductRepo;
import org.example.repo.filter.FilterParam;
import org.example.repo.specification.ProductRelationSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamilyProductService {

    private final FamilyRepo familyRepo;
    private final ProductRepo productRepo;
    private final FamilyProductRepo familyProductRepo;
    private final FamilyProductMapper familyProductMapper;

    public Page<ResponseSharedProductDTO> getAllProductsForFamily(Long familyID, FilterParam filterParam, Pageable pageable) {
        Family family = familyRepo.findById(familyID).orElseThrow(() -> new FamilyNotFoundException("Family is not found"));

        Specification<FamilyProduct> spec = ProductRelationSpecification.filterByParams(filterParam, family);

        return familyProductRepo.findAll(spec, pageable).map(familyProductMapper::toResponseSharedProductDTO);
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
    public ResponseSharedProductDTO updateProductStatusForFamily(UpdateProductStatusDTO status, Long familyProductId) {
        FamilyProduct familyProduct = familyProductRepo.findById(familyProductId).orElseThrow(() -> new ProductNotFoundException("Product is not found"));

        try {
            familyProduct.setStatus(ProductStatus.valueOf(status.getStatus()));
        } catch (IllegalArgumentException e) {
            throw new IllegalStatusException("Wrong status for product", Errors.ILLEGAL_STATUS);
        }
        familyProductRepo.save(familyProduct);
        return familyProductMapper.toResponseSharedProductDTO(familyProduct);
    }

    public void deleteProductFromFamily(Long productId) {
        familyProductRepo.deleteById(productId);
    }
}
