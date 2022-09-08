package com.mazanenko.petproject.bookshop.entity;

import com.mazanenko.petproject.bookshop.DTO.BookDto;
import com.mazanenko.petproject.bookshop.DTO.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue(ProductType.Constants.BOOK)
@DynamicUpdate
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

    @OneToMany(mappedBy = "product")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Subscription> subscribersList = new ArrayList<>();

    public Book(String name, String description, Integer availableQuantity, BigDecimal price, String author,
                String genre, String photoURL) {
        super(name, description, availableQuantity, price);
        this.author = author;
        this.genre = genre;
        this.photoURL = photoURL;
    }


    @Override
    public String toString() {
        return "Book{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", author='" + getAuthor() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        if (!super.equals(o)) return false;

        if (!Objects.equals(author, book.author)) return false;
        if (!Objects.equals(genre, book.genre)) return false;
        return Objects.equals(photoURL, book.photoURL);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (photoURL != null ? photoURL.hashCode() : 0);
        return result;
    }

    @Override
    public ProductDto getDto() {
        return new BookDto();
    }

    @Override
    public Class<BookDto> getDtoClass() {
        return BookDto.class;
    }
}
