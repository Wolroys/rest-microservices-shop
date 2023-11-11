package com.wolroys.shopentity.mapper;

import com.wolroys.shopentity.dto.OrderProductDto;
import com.wolroys.shopentity.dto.cart.CartDto;
import com.wolroys.shopentity.dto.cart.CartProductDto;
import com.wolroys.shopentity.entity.Cart;
import com.wolroys.shopentity.entity.CartProduct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final ModelMapper modelMapper;

    public CartDto toCartDto(Cart cart){
        return cart == null ? null : modelMapper.map(cart, CartDto.class);
    }

    public Cart toCart(CartDto cartDto){
        return cartDto == null ? null : modelMapper.map(cartDto, Cart.class);
    }

    public CartProductDto toCartProductDto(CartProduct cartProduct){
        return cartProduct == null ? null : modelMapper.map(cartProduct, CartProductDto.class);
    }

    public CartProduct toCartProduct(CartProductDto cartProductDto){
        return cartProductDto == null ? null : modelMapper.map(cartProductDto, CartProduct.class);
    }

    public OrderProductDto toOrderProductDto(CartProductDto cartProductDto){
        return cartProductDto == null ? null : modelMapper.map(cartProductDto, OrderProductDto.class);
    }
}
