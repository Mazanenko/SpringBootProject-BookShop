package com.mazanenko.petproject.firstspringcrudapp.dao.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.DAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.mapper.BookMapper;
import com.mazanenko.petproject.firstspringcrudapp.dao.mapper.ProductPhotoMapper;
import com.mazanenko.petproject.firstspringcrudapp.entity.ProductPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductPhotoDAO implements DAO<ProductPhoto> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductPhotoDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(ProductPhoto entity) {

    }

    @Override
    public ProductPhoto read(int productId) {
        return jdbcTemplate.query("SELECT * FROM product_photo WHERE product_id = ?", new ProductPhotoMapper(), productId)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<ProductPhoto> readAll() {
        return null;
    }

    @Override
    public void update(int id, ProductPhoto entity) {

    }

    @Override
    public void delete(int id) {

    }
}
