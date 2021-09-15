package com.mazanenko.petproject.firstspringcrudapp.dao.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.DAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.mapper.BookMapper;
import com.mazanenko.petproject.firstspringcrudapp.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDAO implements DAO<Book> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Book book) {
        jdbcTemplate.update("INSERT INTO book (name, author, genre, description, available_quantity, price) VALUES (?, ?, ?, ?, ?, ?)",
                book.getName(), book.getAuthor(), book.getGenre(), book.getDescription(), book.getAvailableQuantity(), book.getPrice());
    }

    @Override
    public Book read(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id = ?", new BookMapper(), id)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Book> readAll() {
        return jdbcTemplate.query("SELECT * FROM book", new BookMapper());
    }

    @Override
    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE book SET name = ?, author = ?, genre = ?, description = ?, available_quantity = ?, price = ? WHERE id = ?",
                book.getName(), book.getAuthor(), book.getGenre(), book.getDescription(), book.getAvailableQuantity(), book.getPrice(), id);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }
}
