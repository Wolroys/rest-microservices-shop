package com.wolroys.cartservice.service;

import com.wolroys.cartservice.repository.CartProductRepository;
import com.wolroys.cartservice.repository.CartRepository;
import com.wolroys.shopentity.dto.ProductDto;
import com.wolroys.shopentity.dto.cart.CartDto;
import com.wolroys.shopentity.entity.Cart;
import com.wolroys.shopentity.entity.CartProduct;
import com.wolroys.shoputils.webClient.ProductWebClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final ProductWebClientBuilder productWebClientBuilder;
    private final CartProductService cartProductService;
    private final CartProductRepository cartProductRepository;

    public Cart createCart(Long userId){
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartRepository.save(cart);
    }

    public Cart getCartByUserId(Long userId){
        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public void addProduct(Long userId, Long productId){
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null)
            cart = createCart(userId);


        if (cartProductRepository.findByProductId(productId) == null) {
            ProductDto productDto = productWebClientBuilder.getProductDto(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                            "Product wasn't founded: " + productId));

            CartProduct cartProduct = cartProductService.addProduct(cart, productDto);
            cart.getProducts().add(cartProduct);
            cart.setTotalPrice(cart.getTotalPrice() + cartProduct.getPrice());

            cartRepository.save(cart);
        } else
            increaseQuantity(userId, productId);

        if (cart.getTotalPrice() == null)
            cart.setTotalPrice(0.0);

    }

    @Transactional
    public void increaseQuantity(Long userId, Long productId){
        CartProduct cartProduct = cartProductRepository.findByProductId(productId);
        cartProduct.setQuantity(cartProduct.getQuantity() + 1);
        Cart cart = cartRepository.findByUserId(userId);
        cart.setTotalPrice(cart.getTotalPrice() + cartProduct.getPrice());

        cartRepository.save(cart);
    }

    @Transactional
    public void decreaseQuantity(Long userId, Long productId){
        CartProduct cartProduct = cartProductRepository.findByProductId(productId);
        cartProduct.setQuantity(cartProduct.getQuantity() - 1);
        Cart cart = cartRepository.findByUserId(userId);
        cart.setTotalPrice(cart.getTotalPrice() - cartProduct.getPrice());

        cartRepository.save(cart);
    }

    @Transactional
    public void removeProduct(Long userId, Long productId){
        CartProduct cartProduct = cartProductRepository.findByProductId(productId);
        Cart cart = cartRepository.findByUserId(userId);

        cart.setTotalPrice(cart.getTotalPrice() - cartProduct.getPrice() * cartProduct.getQuantity());

        cart.getProducts().remove(cartProduct);
        cartProductRepository.delete(cartProduct);
        cartRepository.save(cart);
    }

    @Transactional
    public void clear(Long userId){
        Cart cart = cartRepository.findByUserId(userId);
        cartProductRepository.removeAllByCartId(cart.getId());
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }
}

//TODO exceptions