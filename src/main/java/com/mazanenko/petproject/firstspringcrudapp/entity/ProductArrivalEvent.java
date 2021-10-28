package com.mazanenko.petproject.firstspringcrudapp.entity;

public class ProductArrivalEvent {
    private final Book book;


    public ProductArrivalEvent(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }
}
