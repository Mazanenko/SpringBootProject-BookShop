package com.mazanenko.petproject.firstspringcrudapp.dao.mapper;

import com.mazanenko.petproject.firstspringcrudapp.entity.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        Book book = new Book();

        book.setId(resultSet.getInt("id"));
        book.setName(resultSet.getString("name"));
        book.setPrice(resultSet.getInt("price"));
        book.setAuthor(resultSet.getString("author"));
        book.setGenre(resultSet.getString("genre"));
        book.setPhotoURL(resultSet.getString("photo_url"));
        book.setDescription(resultSet.getString("description"));
        book.setAvailableQuantity(resultSet.getInt("available_quantity"));

        return  book;
    }
}
