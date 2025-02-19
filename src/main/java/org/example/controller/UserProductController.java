package org.example.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.UpdateProductStatusDTO;
import org.example.dto.response.PageDTO;
import org.example.dto.response.ResponseSharedProductDTO;
import org.example.repo.filter.FilterParam;
import org.example.service.UserProductService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("newAPI/solo/product")
@RequiredArgsConstructor
public class UserProductController {

    private final UserProductService userProductService;

    @GetMapping
    public PageDTO<ResponseSharedProductDTO> getAllProductsForUser(@ParameterObject @Valid @Parameter FilterParam filterParam, Pageable pageable) {
        Page<ResponseSharedProductDTO> productInfoPage = userProductService.getAllProductsForUser(filterParam, pageable);
        return new PageDTO<>(
                productInfoPage.getContent(),
                productInfoPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @PostMapping
    public ResponseEntity<ResponseSharedProductDTO> addProductForYourself(@RequestParam Long productID) {
        return ResponseEntity.ok(userProductService.addProductToYourself(productID));
    }

    @PutMapping("{productId}")
    public ResponseEntity<ResponseSharedProductDTO> updateStatusSolo(@RequestBody UpdateProductStatusDTO status, @PathVariable Long productId) {
        return ResponseEntity.ok(userProductService.updateProductStatusForUser(status, productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProductForUser(@RequestParam Long productID) {
        userProductService.deleteProductFromUser(productID);
        return ResponseEntity.noContent().build();
    }

}
