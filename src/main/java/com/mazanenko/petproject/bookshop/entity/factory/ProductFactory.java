package com.mazanenko.petproject.bookshop.entity.factory;

import com.mazanenko.petproject.bookshop.entity.Product;
import com.mazanenko.petproject.bookshop.entity.ProductType;

public interface ProductFactory {
    Product create(ProductType type);

    Class<? extends Product> getClass(ProductType type);
}
