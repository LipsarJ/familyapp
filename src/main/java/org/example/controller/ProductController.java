package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.response.ResponseProductDTO;
import org.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("newAPI/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("solo")
    public ResponseEntity<List<ResponseProductDTO>> getAllProductsForUser() {
        return ResponseEntity.ok(productService.getAllProductsForUser());
    }
}
