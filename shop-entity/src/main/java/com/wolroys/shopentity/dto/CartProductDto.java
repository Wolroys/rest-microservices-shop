package com.wolroys.shopentity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDto {

    private Long productId;

    private int quantity;

    private Double price;
}
