package com.wolroys.cartservice.service;

import com.wolroys.cartservice.repository.CartProductRepository;
import com.wolroys.shopentity.dto.ProductDto;
import com.wolroys.shopentity.entity.Cart;
import com.wolroys.shopentity.entity.CartProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartProductService {

    private final CartProductRepository cartProductRepository;

    public CartProduct addProduct(Cart cart, ProductDto productDto){
        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProductId(productDto.getId());
        cartProduct.setQuantity(1);
        cartProduct.setPrice(productDto.getPrice());

        return cartProduct;
    }
}
