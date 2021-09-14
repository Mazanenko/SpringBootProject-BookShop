package com.mazanenko.petproject.firstspringcrudapp.service;

import com.mazanenko.petproject.firstspringcrudapp.entity.Book;

import java.util.List;

public interface BookService {
    void createBook(Book book);

    Book getBookById(int id);

    List<Book> getAllBooks();

    void updateBookById(int id, Book updatedBook);

    void deleteBookById(int id);
}
