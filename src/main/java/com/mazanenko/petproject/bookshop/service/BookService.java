package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Book;

import java.sql.SQLException;
import java.util.List;

public interface BookService {
    void createBook(Book book);

    Book getBookById(Long bookId);

    List<Book> getAllBooks();

    void updateBookById(Long bookId, Book updatedBook);

    void setQuantity(Long bookId, int newQuantity);

    void incrementBookQuantity(Long bookId);

    void decrementBookQuantity(Long bookId) throws SQLException;

    void deleteBookById(Long bookId);

    boolean isBookAvailable(Long bookId);
}
