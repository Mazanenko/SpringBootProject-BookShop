package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.DTO.ProductDto;
import com.mazanenko.petproject.bookshop.annotation.LogException;
import com.mazanenko.petproject.bookshop.entity.Product;
import com.mazanenko.petproject.bookshop.entity.event.ProductArrivalEvent;
import com.mazanenko.petproject.bookshop.repository.ProductRepository;
import com.mazanenko.petproject.bookshop.service.ProductService;
import com.mazanenko.petproject.bookshop.utill.modelMapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Can't find product with id: ";
    private final ProductRepository productRepo;
    private final ProductMapper productMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<ProductDto> findAll() {
        return productRepo.findAll().stream().map(productMapper::toDto).toList();
    }

    @Override
    public ProductDto findOneById(@NonNull Long productId) {
        return productMapper.toDto(findProductById(productId));
    }

    @Override
    public ProductDto create(@NonNull ProductDto productDto) {
        return productMapper.toDto(productRepo.save(productMapper.toEntity(productDto)));
    }

    @Override
    public ProductDto update(@NonNull Long productId, @NonNull ProductDto updatedDto) {
        Product productFromDb = findProductById(productId);
        productMapper.updateFromDto(productFromDb, updatedDto);
        return productMapper.toDto(productRepo.save(productFromDb));
    }

    @Override
    public void delete(@NonNull Long productId) {
        productRepo.deleteById(productId);
    }

    @Override
    public void setNewQuantity(@NonNull Long productId, Integer newQuantity) {
        if (newQuantity < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "newQuantity can't be less than 0");
        }
        Product productFromDb = findProductById(productId);

        if (productFromDb.getAvailableQuantity() == 0 && newQuantity > 0) {
            publishArrivalEvent(productFromDb);
        }
        productFromDb.setAvailableQuantity(newQuantity);
        productRepo.save(productFromDb);

        log.info("New available quantity for the product {} with ID {} is {}",
                productFromDb.getName(), productId, newQuantity);
    }

    @Override
    public void incrementProductQuantity(@NonNull Product product) {
        if (product.getId() < 1L) {
            log.warn("Product id can't be less than 1");
            return;
        }
        int quantity = product.getAvailableQuantity();
        product.setAvailableQuantity(++quantity);
        productRepo.save(product);

        log.info("Increment quantity. New available quantity for the product {} with ID {} is {}",
                product.getName(), product.getId(), product.getAvailableQuantity());
    }

    @Override
    @LogException
    public void decrementProductQuantity(@NonNull Product product) {
        if (product.getId() < 1L) {
            log.warn("Product id can't be less than 1");
            return;
        }
        int quantity = product.getAvailableQuantity();
        if (quantity < 1) {
            throw new IllegalArgumentException("Can't decrement quantity, because available quantity is less than one");
        }
        product.setAvailableQuantity(--quantity);
        productRepo.save(product);

        log.info("Decrement quantity. New available quantity for the product {} with ID {} is {}",
                product.getName(), product.getId(), product.getAvailableQuantity());
    }

    @Override
    public boolean isProductAvailable(@NonNull Long productId) {
        if (productId < 1L) {
            log.warn("Product id can't be less than 1");
            return false;
        }
        return findProductById(productId).getAvailableQuantity() > 0;
    }

    private Product findProductById(Long productId) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + productId));
    }

    private void publishArrivalEvent(@NonNull Product product) {
        applicationEventPublisher.publishEvent(new ProductArrivalEvent(product));
    }
}
