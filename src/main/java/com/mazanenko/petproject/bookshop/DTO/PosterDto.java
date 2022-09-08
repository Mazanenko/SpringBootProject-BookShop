package com.mazanenko.petproject.bookshop.DTO;

import lombok.*;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PosterDto extends ProductDto {
    private String name;
    private String theme;
    private String description;
    private Integer height;
    private Integer width;
    private BigDecimal price;
    private Integer availableQuantity;
}
