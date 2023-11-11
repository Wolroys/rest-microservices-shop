package com.wolroys.shopentity.mapper;

import com.wolroys.shopentity.dto.OrderDto;
import com.wolroys.shopentity.dto.OrderProductDto;
import com.wolroys.shopentity.entity.Order;
import com.wolroys.shopentity.entity.OrderProduct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ModelMapper modelMapper;

    public OrderDto toOrderDto(Order order){
        return order == null ? null : modelMapper.map(order, OrderDto.class);
    }

    public Order toOrder(OrderDto orderDto){
        return orderDto == null ? null : modelMapper.map(orderDto, Order.class);
    }

    public OrderProduct toOrderProduct(OrderProductDto orderProductDto){
        return orderProductDto == null ? null : modelMapper.map(orderProductDto, OrderProduct.class);
    }

    public OrderProductDto toOrderProductDto(OrderProduct orderProduct){
        return orderProduct == null ? null : modelMapper.map(orderProduct, OrderProductDto.class);
    }
}
