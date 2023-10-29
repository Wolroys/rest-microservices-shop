package com.wolroys.shopentity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateEditDto {

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String categoryTitle;

    private Long sellerId;
}
