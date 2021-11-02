package com.mazanenko.petproject.bookshop.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class Book extends Product {

    @NotBlank(message = "Should be not empty")
    private String author;

    @NotBlank(message = "Should be not empty")
    private String genre;

    @Pattern(regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,4}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)",
    message = "not valid URL")
    private String photoURL;

    public Book() {}

    public Book(int id, String name, String description, int availableQuantity, String author, String genre,
                String photoURL) {
        super(id, name, description, availableQuantity);
        this.author = author;
        this.genre = genre;
        this.photoURL = photoURL;
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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                "author='" + getAuthor() + '\'' +
                ", availableQuantity='" + getAvailableQuantity() + '\'' +
                '}';
    }
}
