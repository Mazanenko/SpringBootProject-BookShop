package com.mazanenko.petproject.bookshop.dao.impl;

import com.mazanenko.petproject.bookshop.dao.BookDAO;
import com.mazanenko.petproject.bookshop.dao.mapper.BookMapper;
import com.mazanenko.petproject.bookshop.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDAOImpl implements BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Book book) {
        jdbcTemplate.update("INSERT INTO book (name, author, genre, description, photo_url, available_quantity, price) VALUES (?, ?, ?, ?, ?, ?, ?)",
                book.getName(), book.getAuthor(), book.getGenre(), book.getDescription(), book.getPhotoURL(),
                book.getAvailableQuantity(), book.getPrice());
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
        jdbcTemplate.update("UPDATE book SET name = ?, author = ?, genre = ?, description = ?, photo_url = ?, available_quantity = ?, price = ? WHERE id = ?",
                book.getName(), book.getAuthor(), book.getGenre(), book.getDescription(), book.getPhotoURL(),
                book.getAvailableQuantity(), book.getPrice(), id);
    }

    @Override
    public void updateQuantity(int bookId, int newQuantity) {
        jdbcTemplate.update("UPDATE book SET available_quantity = ? WHERE id = ?",
                newQuantity, bookId);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }
}
