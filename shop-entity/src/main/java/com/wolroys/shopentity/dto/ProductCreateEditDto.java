package com.wolroys.shopentity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateEditDto {

    @NotBlank(message = "Specify correct name of the item")
    @NotNull(message = "Specify correct name of the item")
    private String name;

    private String description;

    @Min(value = 1)
    private Double price;

    @Min(value = 1)
    private Integer quantity;

    @NotNull(message = "Choose a category")
    private String categoryTitle;

    private Long sellerId;
}
