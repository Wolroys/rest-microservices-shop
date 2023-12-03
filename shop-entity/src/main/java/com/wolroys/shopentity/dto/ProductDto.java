package com.wolroys.shopentity.dto;

import com.wolroys.shopentity.entity.AuditingEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Min(value = 1)
    private Double price;

    @Min(value = 1)
    private Integer quantity;
}
