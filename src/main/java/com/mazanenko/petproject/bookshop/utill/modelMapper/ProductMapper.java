package com.mazanenko.petproject.bookshop.utill.modelMapper;

import com.mazanenko.petproject.bookshop.DTO.ProductDto;
import com.mazanenko.petproject.bookshop.entity.Product;

public interface ProductMapper extends EntityMapper<Product, ProductDto> {
    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);

    void updateFromDto(Product product, ProductDto productDto);
}
