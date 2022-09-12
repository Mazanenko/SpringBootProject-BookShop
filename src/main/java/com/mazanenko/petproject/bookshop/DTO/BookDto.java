package com.mazanenko.petproject.bookshop.DTO;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class BookDto extends ProductDto {
    @NotBlank(message = "Should be not empty")
    private String name;
    @Min(value = 0, message = "Should be greater then zero")
    private BigDecimal price;
    @NotBlank(message = "Should be not empty")
    private String description;
    @PositiveOrZero(message = "Should be positive or zero")
    private Integer availableQuantity;
    @NotBlank(message = "Should be not empty")
    private String author;
    @NotBlank(message = "Should be not empty")
    private String genre;
    @Pattern(regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,4}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)",
            message = "not valid URL")
    private String photoURL;
}
