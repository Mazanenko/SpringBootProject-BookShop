package com.mazanenko.petproject.bookshop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book extends Product {

    @NotBlank(message = "Should be not empty")
    @Column(name = "author")
    private String author;

    @NotBlank(message = "Should be not empty")
    @Column(name = "genre")
    private String genre;

    @Pattern(regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,4}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)",
    message = "not valid URL")
    @Column(name = "photo_url")
    private String photoURL;

    @OneToMany(mappedBy = "book")
    private List<Order> orderList;

    @OneToMany(mappedBy = "book")
    private List<Subscription> subscribersList;

    public Book() {}

    public Book(Long id, String name, String description, int availableQuantity, int price, String author,
                String genre, String photoURL) {
        super(id, name, description, availableQuantity, price);
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

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<Subscription> getSubscribersList() {
        return subscribersList;
    }

    public void setSubscribersList(List<Subscription> subscribersList) {
        this.subscribersList = subscribersList;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Book book = (Book) o;
        return author.equals(book.author) && Objects.equals(genre, book.genre)
                && Objects.equals(photoURL, book.photoURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), author, genre, photoURL);
    }
}
