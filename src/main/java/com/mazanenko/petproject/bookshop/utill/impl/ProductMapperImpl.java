package com.mazanenko.petproject.bookshop.utill.impl;

import com.mazanenko.petproject.bookshop.DTO.ProductDto;
import com.mazanenko.petproject.bookshop.entity.Product;
import com.mazanenko.petproject.bookshop.entity.factory.ProductFactory;
import com.mazanenko.petproject.bookshop.utill.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ProductMapperImpl implements ProductMapper {

    private final ModelMapper modelMapper;
    private final ProductFactory productFactory;

    @Override
    public Product toEntity(ProductDto productDto) {
        return Objects.isNull(productDto) ? null : modelMapper.map(productDto,
                productFactory.getClass(productDto.getProductType()));
    }

    @Override
    public ProductDto toDto(Product product) {
        return Objects.isNull(product) ? null : modelMapper.map(product, product.getDtoClass());
    }

    @Override
    public void updateFromDto(Product product, ProductDto productDto) {
        if (productDto == null) throw new IllegalArgumentException("productDto can't bee null.");
        if (product == null) throw new IllegalArgumentException("product can't bee null.");
        modelMapper.map(productDto, product);
    }
}
