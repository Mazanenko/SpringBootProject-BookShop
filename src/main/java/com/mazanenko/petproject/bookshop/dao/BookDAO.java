package com.mazanenko.petproject.bookshop.dao;

import com.mazanenko.petproject.bookshop.entity.Book;

import java.util.List;

public interface BookDAO {

    void create(Book book);

    Book read(int id);

    List<Book> readAll();

    void update(int id, Book book);

    void updateQuantity(int bookId, int newQuantity);

    void delete(int id);
}
