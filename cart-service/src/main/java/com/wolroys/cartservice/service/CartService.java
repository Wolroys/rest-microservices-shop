package com.wolroys.cartservice.service;

import com.wolroys.cartservice.repository.CartRepository;
import com.wolroys.shopentity.dto.ProductDto;
import com.wolroys.shopentity.dto.cart.CartDto;
import com.wolroys.shopentity.entity.Cart;
import com.wolroys.shopentity.entity.CartProduct;
import com.wolroys.shoputils.webClient.ProductWebClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductWebClientBuilder productWebClientBuilder;
    private final CartProductService cartProductService;

    public Cart createCart(Long userId){
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartRepository.save(cart);
    }

    public Cart getCartByUserId(Long userId){
        return cartRepository.findByUserId(userId);
    }

    public void addProduct(Long userId, Long productId){
        Cart cart = cartRepository.findByUserId(userId);
        ProductDto productDto = productWebClientBuilder.getProductDto(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                        "Product wasn't founded: " + productId));

        CartProduct cartProduct = cartProductService.addProduct(cart, productDto);
        cart.getProducts().add(cartProduct);

        cartRepository.save(cart);
    }
}
