package com.wolroys.shopentity.dto;

import com.wolroys.shopentity.entity.AuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto  {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer quantity;
}
