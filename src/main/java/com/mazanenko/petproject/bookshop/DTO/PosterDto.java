package com.mazanenko.petproject.bookshop.DTO;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PosterDto extends ProductDto {
    @NotBlank(message = "Should be not empty")
    private String name;
    @NotBlank(message = "Should be not empty")
    private String theme;
    private String description;
    @Min(value = 0, message = "Should be greater then zero")
    private Integer height;
    @Min(value = 0, message = "Should be greater then zero")
    private Integer width;
    @Min(value = 0, message = "Should be greater then zero")
    private BigDecimal price;
    @PositiveOrZero(message = "Should be positive or zero")
    private Integer availableQuantity;
}
