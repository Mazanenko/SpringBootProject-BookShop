package com.mazanenko.petproject.firstspringcrudapp.dao;

import com.mazanenko.petproject.firstspringcrudapp.entity.Book;

import java.util.List;

public interface BookDAO {

    void create(Book book);

    Book read(int id);

    List<Book> readAll();

    void update(int id, Book book);

    void delete(int id);
}
