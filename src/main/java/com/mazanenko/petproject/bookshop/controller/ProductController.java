package com.mazanenko.petproject.bookshop.controller;

import com.mazanenko.petproject.bookshop.DTO.ProductDto;
import com.mazanenko.petproject.bookshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ProductDto> getProduct(@PathVariable("id") Long productId) {
        checkId(productId);
        return ResponseEntity.ok(productService.findOneById(productId));
    }

    @PostMapping
    public ResponseEntity<? extends ProductDto> createProduct(@RequestBody ProductDto productDto) {
        if (productDto == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends ProductDto> updateProduct(@PathVariable("id") Long productId,
                                                              @RequestBody ProductDto updatedDto) {
        checkId(productId);
        return ResponseEntity.ok(productService.update(productId, updatedDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long productId) {
        checkId(productId);
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }


    private void checkId(Long productId) {
        if (productId == null || productId < 1L) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
