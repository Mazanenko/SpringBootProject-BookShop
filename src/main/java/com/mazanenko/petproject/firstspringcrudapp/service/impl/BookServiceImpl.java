package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.BookDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Book;
import com.mazanenko.petproject.firstspringcrudapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void deleteBookById(int id) {
        bookDAO.delete(id);
    }
}
