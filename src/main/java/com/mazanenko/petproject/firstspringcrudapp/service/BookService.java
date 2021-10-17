package com.mazanenko.petproject.firstspringcrudapp.service;

import com.mazanenko.petproject.firstspringcrudapp.entity.Book;

import java.sql.SQLException;
import java.util.List;

public interface BookService {
    void createBook(Book book);

    Book getBookById(int id);

    List<Book> getAllBooks();

    void updateBookById(int id, Book updatedBook);

    void setQuantity(int bookId, int newQuantity);

    void incrementBookQuantity(int bookId);

    void decrementBookQuantity(int bookId) throws SQLException;

    void deleteBookById(int id);

    boolean isBookAvailable(int bookId);
}
