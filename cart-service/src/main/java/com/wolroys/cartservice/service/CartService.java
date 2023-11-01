package com.wolroys.cartservice.service;

import com.wolroys.cartservice.repository.CartRepository;
import com.wolroys.shopentity.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart createCart(Long userId){
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartRepository.save(cart);
    }

    public Cart getCartByUserId(Long userId){
        return cartRepository.findByUserId(userId);
    }
}
