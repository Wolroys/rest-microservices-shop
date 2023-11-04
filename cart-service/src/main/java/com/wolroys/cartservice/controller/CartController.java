package com.wolroys.cartservice.controller;

import com.wolroys.cartservice.service.CartProductService;
import com.wolroys.cartservice.service.CartService;
import com.wolroys.shopentity.dto.cart.CartDto;
import com.wolroys.shopentity.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    private final CartProductService cartProductService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartForUserId(@PathVariable Long userId){
        Cart cart = cartService.getCartByUserId(userId);
        if (cart  == null) {
            cart = cartService.createCart(userId);
        }
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/items")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addItemCart(@PathVariable Long userId, @RequestBody Long productId){
        cartService.addProduct(userId, productId);
    }
}
