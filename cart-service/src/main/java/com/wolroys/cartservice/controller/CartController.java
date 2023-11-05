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
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/items")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addProductCart(@PathVariable Long userId, @RequestBody Long productId){
        cartService.addProduct(userId, productId);
    }

    @PutMapping("/{userId}/items/increase")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void increaseQuantity(@PathVariable Long userId, @RequestBody Long productId){
        cartService.increaseQuantity(userId, productId);
    }

    @PutMapping("/{userId}/items/decrease")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void decreaseQuantity(@PathVariable Long userId, @RequestBody Long productId){
        cartService.decreaseQuantity(userId, productId);
    }

    @DeleteMapping("/{userId}")
    public void removeProduct(@PathVariable Long userId, @RequestBody Long productId){
        cartService.removeProduct(userId, productId);
    }

    @DeleteMapping("/clear/{userId}")
    public void clear(@PathVariable Long userId){
        cartService.clear(userId);
    }
}
