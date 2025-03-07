package org.example.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.RequestProductDTO;
import org.example.dto.response.PageDTO;
import org.example.dto.response.ResponseProductDTO;
import org.example.repo.filter.FilterParam;
import org.example.service.ProductService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("newAPI/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public PageDTO<ResponseProductDTO> getAllProducts(@ParameterObject @Valid @Parameter FilterParam filterParam,
                                                      Pageable pageable) {
        Page<ResponseProductDTO> productInfoPage = productService.getAllProducts(filterParam, pageable);
        return new PageDTO<>(
                productInfoPage.getContent(),
                productInfoPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @PostMapping
    public ResponseEntity<ResponseProductDTO> createProduct(@RequestBody RequestProductDTO requestProductDTO) {
        return ResponseEntity.ok(productService.addNewProduct(requestProductDTO));
    }


    @PutMapping("{productId}")
    public ResponseEntity<ResponseProductDTO> updateProduct(@RequestBody RequestProductDTO requestProductDTO, @PathVariable Long productId) {
        return ResponseEntity.ok(productService.updateProduct(requestProductDTO, productId));
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }


}
