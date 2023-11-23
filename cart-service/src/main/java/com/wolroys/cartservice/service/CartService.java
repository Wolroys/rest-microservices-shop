package com.wolroys.cartservice.service;

import com.wolroys.cartservice.mq.CartProducerManager;
import com.wolroys.cartservice.repository.CartProductRepository;
import com.wolroys.cartservice.repository.CartRepository;
import com.wolroys.shopentity.dto.OrderDto;
import com.wolroys.shopentity.dto.ProductDto;
import com.wolroys.shopentity.dto.CartDto;
import com.wolroys.shopentity.dto.CartProductDto;
import com.wolroys.shopentity.entity.Cart;
import com.wolroys.shopentity.entity.CartProduct;
import com.wolroys.shopentity.entity.Status;
import com.wolroys.shopentity.mapper.CartMapper;
import com.wolroys.shoputils.webClient.ProductWebClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final ProductWebClientBuilder productWebClientBuilder;
    private final CartProductService cartProductService;
    private final CartProductRepository cartProductRepository;
    private final CartMapper cartMapper;
    private final CartProducerManager cartProducerManager;

    @Transactional
    public Cart createCart(Long userId){
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setTotalPrice(0.0);
        return cartRepository.save(cart);
    }

    @Transactional
    public CartDto getCartByUserId(Long userId){
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null)
            cart = createCart(userId);
        return cartMapper.toCartDto(cart);
    }

    @Transactional
    public CartProductDto addProduct(Long userId, Long productId){
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null)
            cart = createCart(userId);

        CartProduct cartProduct = null;

        if (cartProductRepository.findByProductId(productId) == null) {
            ProductDto productDto = productWebClientBuilder.getProductDto(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Product wasn't founded: " + productId));

            cartProduct = cartProductService.addProduct(cart, productDto);
            cart.getProducts().add(cartProduct);
            cart.setTotalPrice(cart.getTotalPrice() + cartProduct.getPrice());

            cartRepository.save(cart);
        } else
            return increaseQuantity(userId, productId);


        return cartMapper.toCartProductDto(cartProduct);
    }

    @Transactional
    public CartProductDto increaseQuantity(Long userId, Long productId){
        CartProduct cartProduct = cartProductRepository.findByProductId(productId);
        cartProduct.setQuantity(cartProduct.getQuantity() + 1);

        Cart cart = cartRepository.findByUserId(userId);
        cart.setTotalPrice(cart.getTotalPrice() + cartProduct.getPrice());

        cartRepository.save(cart);

        return cartMapper.toCartProductDto(cartProduct);
    }

    @Transactional
    public CartProductDto decreaseQuantity(Long userId, Long productId){

        CartProduct cartProduct = cartProductRepository.findByProductId(productId);
        cartProduct.setQuantity(cartProduct.getQuantity() - 1);

        Cart cart = cartRepository.findByUserId(userId);
        cart.setTotalPrice(cart.getTotalPrice() - cartProduct.getPrice());

        cartRepository.save(cart);

        return cartMapper.toCartProductDto(cartProduct);
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
        cartRepository.delete(cart);
        createCart(userId);
    }

    @Transactional
    public OrderDto createOrder(Long userId){
        CartDto cart = getCartByUserId(userId);

        if (cart != null && !cart.getProducts().isEmpty()){
            OrderDto orderDto = new OrderDto();

            orderDto.setUserId(userId);
            orderDto.setOrderDate(LocalDate.now());
            orderDto.setStatus(Status.PAID);
            orderDto.setProducts(cart.getProducts()
                    .stream()
                    .map(cartMapper::toOrderProductDto)
                    .toList());
            orderDto.setTotalAmount(cart.getTotalPrice());

            clear(userId);

            cartProducerManager.sendNewCartMessage(orderDto);

            return orderDto;
        }
        return null;
    }
}

//TODO exceptions