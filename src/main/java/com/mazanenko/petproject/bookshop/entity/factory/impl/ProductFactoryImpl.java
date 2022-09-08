package com.mazanenko.petproject.bookshop.entity.factory.impl;

import com.mazanenko.petproject.bookshop.entity.Book;
import com.mazanenko.petproject.bookshop.entity.Poster;
import com.mazanenko.petproject.bookshop.entity.Product;
import com.mazanenko.petproject.bookshop.entity.ProductType;
import com.mazanenko.petproject.bookshop.entity.factory.ProductFactory;
import org.springframework.stereotype.Component;

@Component
public class ProductFactoryImpl implements ProductFactory {

    @Override
    public Product create(ProductType type) {
        switch (type) {
            case BOOK -> {return new Book();}
            case POSTER -> {return new Poster();}
            default -> throw new IllegalArgumentException("Wrong product type: " + type);
        }
    }

    @Override
    public Class<? extends Product> getClass(ProductType type) {
        switch (type) {
            case BOOK -> {return Book.class;}
            case POSTER -> {return Poster.class;}
            default -> throw new IllegalArgumentException("Wrong product type: " + type);
        }
    }
}
