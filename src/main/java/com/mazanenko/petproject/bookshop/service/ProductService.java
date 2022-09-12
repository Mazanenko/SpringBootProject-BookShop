package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.DTO.ProductDto;
import com.mazanenko.petproject.bookshop.entity.Product;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();

    ProductDto findOneById(@NonNull Long productId);

    ProductDto create(@NonNull ProductDto productDto);

    ProductDto update(@NonNull Long productId, @NonNull ProductDto updatedDto);

    void delete(@NonNull Long productId);

    void setNewQuantity(@NonNull Long productId, Integer newQuantity);

    void incrementProductQuantity(@NonNull Product product);

    void decrementProductQuantity(@NonNull Product product);

    boolean isProductAvailable(@NonNull Long productId);
}
