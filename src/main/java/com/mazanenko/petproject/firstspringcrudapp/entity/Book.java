package com.mazanenko.petproject.firstspringcrudapp.entity;

public class Book extends Product {
    private String author;
    private String genre;

    public Book() {}

    public Book(int id, String name, String description, String availableQuantity, ProductPhoto productPhoto,
                String author, String genre) {
        super(id, name, description, availableQuantity, productPhoto);
        this.author = author;
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
