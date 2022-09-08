package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.DTO.ProductDto;
import com.mazanenko.petproject.bookshop.entity.Product;
import com.mazanenko.petproject.bookshop.repository.ProductRepository;
import com.mazanenko.petproject.bookshop.service.ProductService;
import com.mazanenko.petproject.bookshop.utill.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Can't find product with id: ";
    private final ProductRepository productRepo;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepo.findAll();
        return products.stream().map(productMapper::toDto).toList();
    }

    @Override
    public ProductDto findOneById(@NonNull Long productId) {
       Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + productId));
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto create(@NonNull ProductDto productDto) {
        return productMapper.toDto(productRepo.save(productMapper.toEntity(productDto)));
    }

    @Override
    public ProductDto update(@NonNull Long productId,
                             @NonNull ProductDto updatedDto) {

        Product productFromDb = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + productId));

        productMapper.updateFromDto(productFromDb, updatedDto);
        return productMapper.toDto(productRepo.save(productFromDb));
    }

    @Override
    public void delete(@NonNull Long productId) {
        productRepo.deleteById(productId);
    }
}
