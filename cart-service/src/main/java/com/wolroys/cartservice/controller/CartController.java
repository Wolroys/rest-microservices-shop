package com.wolroys.cartservice.controller;

import com.wolroys.cartservice.service.CartProductService;
import com.wolroys.cartservice.service.CartService;
import com.wolroys.shopentity.dto.cart.CartDto;
import com.wolroys.shopentity.dto.cart.CartProductDto;
import com.wolroys.shopentity.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    private final CartProductService cartProductService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartForUserId(@PathVariable Long userId){
        CartDto cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/items")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<CartProductDto> addProductCart(@PathVariable Long userId, @RequestBody Long productId){
        try {
            return ResponseEntity.ok(cartService.addProduct(userId, productId));
        } catch (ResponseStatusException e) {
            return new ResponseEntity("This product is out of stock", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{userId}/items/increase")
    public ResponseEntity<CartProductDto> increaseQuantity(@PathVariable Long userId, @RequestBody Long productId){
        return ResponseEntity.ok(cartService.increaseQuantity(userId, productId));
    }

    @PutMapping("/{userId}/items/decrease")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CartProductDto decreaseQuantity(@PathVariable Long userId, @RequestBody Long productId){
        return cartService.decreaseQuantity(userId, productId);
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
