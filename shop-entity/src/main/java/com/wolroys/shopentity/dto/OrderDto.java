package com.wolroys.shopentity.dto;

import com.wolroys.shopentity.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;

    private Long userId;

    private LocalDate orderDate;

    private Status status;

    private List<OrderProductDto> products;

    private double totalAmount;
}
