package com.mazanenko.petproject.bookshop.utill;

import com.mazanenko.petproject.bookshop.DTO.ProductDto;
import com.mazanenko.petproject.bookshop.entity.Product;

public interface ProductMapper {
    Product toEntity(ProductDto productDto);
    ProductDto toDto(Product product);
    void updateFromDto(Product product, ProductDto productDto);
}
