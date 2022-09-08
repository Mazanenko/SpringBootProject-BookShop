package com.mazanenko.petproject.bookshop.DTO;

import lombok.*;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class BookDto extends ProductDto {
    private String name;
    private BigDecimal price;
    private String description;
    private Integer availableQuantity;
    private String author;
    private String genre;
    private String photoURL;
}
