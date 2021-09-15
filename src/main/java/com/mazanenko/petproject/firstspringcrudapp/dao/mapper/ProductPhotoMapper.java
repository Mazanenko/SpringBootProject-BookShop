package com.mazanenko.petproject.firstspringcrudapp.dao.mapper;

import com.mazanenko.petproject.firstspringcrudapp.entity.ProductPhoto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductPhotoMapper implements RowMapper<ProductPhoto> {
    @Override
    public ProductPhoto mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductPhoto photo = new ProductPhoto();

        photo.setProductId(resultSet.getInt("product_id"));
        photo.setURL(resultSet.getString("url"));

        return photo;
    }
}
