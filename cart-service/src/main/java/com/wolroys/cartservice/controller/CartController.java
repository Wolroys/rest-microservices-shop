package com.wolroys.cartservice.controller;

import com.wolroys.cartservice.service.CartProductService;
import com.wolroys.cartservice.service.CartService;
import com.wolroys.shopentity.dto.OrderDto;
import com.wolroys.shopentity.dto.CartDto;
import com.wolroys.shopentity.dto.CartProductDto;
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

    @GetMapping
    public ResponseEntity<CartDto> getCartForUserId(@RequestHeader String username){
        CartDto cart = cartService.getCartByUsername(username);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> addProductCart(@RequestHeader(required = true) String username,
                                                         @RequestBody Long productId){
        try {
            return ResponseEntity.ok(cartService.addProduct(username, productId));
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>("This product is out of stock", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/items/increase")
    public ResponseEntity<CartProductDto> increaseQuantity(@RequestHeader(required = false) String username, @RequestBody Long productId){
        return ResponseEntity.ok(cartService.increaseQuantity(username, productId));
    }

    @PutMapping("/items/decrease")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CartProductDto decreaseQuantity(@RequestHeader(required = false) String username, @RequestBody Long productId){
        return cartService.decreaseQuantity(username, productId);
    }

    @DeleteMapping
    public void removeProduct(@RequestHeader(required = true) String username, @RequestBody Long productId){
        cartService.removeProduct(username, productId);
    }

    @DeleteMapping("/clear")
    public void clear(@RequestHeader(required = true) String username){
        cartService.clear(username);
    }

    @GetMapping("/buy")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto buy(@RequestHeader String username){
        return cartService.createOrder(username);
    }
}
