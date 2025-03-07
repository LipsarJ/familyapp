package org.example.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.UpdateProductStatusDTO;
import org.example.dto.response.PageDTO;
import org.example.dto.response.ResponseSharedProductDTO;
import org.example.repo.filter.FilterParam;
import org.example.service.FamilyProductService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("newAPI/family/{familyId}/product")
@RequiredArgsConstructor
public class FamilyProductController {

    private final FamilyProductService familyProductService;

    @GetMapping
    public PageDTO<ResponseSharedProductDTO> getProductsForFamily(@PathVariable("familyId") Long familyId, @ParameterObject @Valid @Parameter FilterParam filterParam, Pageable pageable) {
        Page<ResponseSharedProductDTO> productInfoPage = familyProductService.getAllProductsForFamily(familyId, filterParam, pageable);
        return new PageDTO<>(
                productInfoPage.getContent(),
                productInfoPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @PostMapping
    public ResponseEntity<ResponseSharedProductDTO> addProductForFamily(@PathVariable("familyId") Long familyID, @RequestParam Long productID) {
        return ResponseEntity.ok(familyProductService.addProductToFamily(familyID, productID));
    }

    @PutMapping("{productId}")
    public ResponseEntity<ResponseSharedProductDTO> updateStatusFamily(@PathVariable Long familyId, @RequestBody UpdateProductStatusDTO status, @PathVariable Long productId) {
        return ResponseEntity.ok(familyProductService.updateProductStatusForFamily(status, productId));
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProductForFamily(@PathVariable Long familyId, @PathVariable Long productId) {
        familyProductService.deleteProductFromFamily(productId);
        return ResponseEntity.noContent().build();
    }

}
