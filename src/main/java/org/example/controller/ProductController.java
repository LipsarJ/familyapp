package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.response.ResponseSharedProductDTO;
import org.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("newAPI/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("{familyId}")
    public ResponseEntity<List<ResponseSharedProductDTO>> getProductsForFamily(@PathVariable("familyId") Long familyId) {
        return ResponseEntity.ok(productService.getAllProductsForFamily(familyId));
    }

    @GetMapping("solo")
    public ResponseEntity<List<ResponseSharedProductDTO>> getAllProductsForUser() {
        return ResponseEntity.ok(productService.getAllProductsForUser());
    }

    @PostMapping("add/{familyID}/{productID}")
    public ResponseEntity<ResponseSharedProductDTO> addProductForFamily(@PathVariable("familyID") Long familyID, @PathVariable Long productID) {
        return ResponseEntity.ok(productService.addProductToFamily(familyID, productID));
    }

    @PostMapping("add/{productID}")
    public ResponseEntity<ResponseSharedProductDTO> addProductForYourself(@PathVariable Long productID) {
        return ResponseEntity.ok(productService.addProductToYourself(productID));
    }
}
