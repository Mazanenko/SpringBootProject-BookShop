package com.mazanenko.petproject.bookshop.entity.event;

import com.mazanenko.petproject.bookshop.entity.Book;

public class ProductArrivalEvent {
    private final Book book;


    public ProductArrivalEvent(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }
}
