package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.BookDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Book;
import com.mazanenko.petproject.firstspringcrudapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookDAO bookDAO;

    @Autowired
    public BookServiceImpl(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public void createBook(Book book) {
        bookDAO.create(book);
    }

    @Override
    public Book getBookById(int id) {
        return bookDAO.read(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDAO.readAll();
    }

    @Override
    public void updateBookById(int id, Book updatedBook) {
        bookDAO.update(id, updatedBook);
    }

    @Override
    public void setQuantity(int bookId, int newQuantity) {
        bookDAO.updateQuantity(bookId, newQuantity);
    }

    @Override
    public void incrementBookQuantity(int bookId) {
        Book book = getBookById(bookId);
        int quantity = book.getAvailableQuantity();
        book.setAvailableQuantity(++quantity);
        updateBookById(bookId, book);
    }

    @Override
    public void decrementBookQuantity(int bookId) throws SQLException {
        Book book = getBookById(bookId);
        int quantity =book.getAvailableQuantity();

        if (quantity >= 1) {
            book.setAvailableQuantity(--quantity);
            updateBookById(bookId, book);
        } else throw new SQLException();
    }

    @Override
    public void deleteBookById(int id) {
        bookDAO.delete(id);
    }

    @Override
    public boolean isBookAvailable(int bookId) {
        Book book = getBookById(bookId);
        return ((book != null) && (book.getAvailableQuantity() > 0));
    }
}
