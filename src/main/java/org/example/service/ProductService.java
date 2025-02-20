package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.controlleradvice.Errors;
import org.example.dto.request.RequestProductDTO;
import org.example.dto.response.ResponseProductDTO;
import org.example.entity.Product;
import org.example.exception.extend.ProductAlreadyExists;
import org.example.exception.extend.ProductNotFoundException;
import org.example.mapper.ProductMapper;
import org.example.repo.ProductRepo;
import org.example.repo.filter.FilterParam;
import org.example.repo.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    public Page<ResponseProductDTO> getAllProducts(FilterParam filterParam, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.searchByFilterText(filterParam.getFilterText());
        return productRepo.findAll(spec, pageable).map(productMapper::toResponseProductDTO);
    }

    @Transactional
    public ResponseProductDTO addNewProduct(RequestProductDTO requestProductDTO) {
        if (productRepo.existsByName(requestProductDTO.getName().toLowerCase())) {
            throw new ProductAlreadyExists("Product cant be added twice", Errors.PRODUCT_ALREADY_EXISTS);
        }
        Product product = new Product();
        product.setName(requestProductDTO.getName().toLowerCase());
        productRepo.save(product);
        return productMapper.toResponseProductDTO(product);
    }

    public ResponseProductDTO updateProduct(RequestProductDTO requestProductDTO, Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setName(requestProductDTO.getName().toLowerCase());
        productRepo.save(product);
        return productMapper.toResponseProductDTO(product);
    }

    public void deleteProduct(Long productId) {
        productRepo.deleteById(productId);
    }


}


