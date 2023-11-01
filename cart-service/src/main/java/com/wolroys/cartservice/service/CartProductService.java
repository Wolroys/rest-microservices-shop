package com.wolroys.cartservice.service;

import com.wolroys.cartservice.repository.CartProductRepository;
import com.wolroys.shopentity.entity.Cart;
import com.wolroys.shopentity.entity.CartProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartProductService {

    private final CartProductRepository cartProductRepository;

    public CartProduct addProduct(Cart cart, Long productId, int quantity){
        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProductId(productId);
        cartProduct.setQuantity(quantity);

        return cartProduct;
    }
}
