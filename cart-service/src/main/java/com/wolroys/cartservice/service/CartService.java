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
    public Cart createCart(String username){
        Cart cart = new Cart();
        cart.setUsername(username);
        cart.setTotalPrice(0.0);
        return cartRepository.save(cart);
    }

    @Transactional
    public CartDto getCartByUsername(String username){
        Cart cart = cartRepository.findByUsername(username);
        if (cart == null)
            cart = createCart(username);
        return cartMapper.toCartDto(cart);
    }

    @Transactional
    public CartProductDto addProduct(String username, Long productId){
        Cart cart = cartRepository.findByUsername(username);

        if (cart == null)
            cart = createCart(username);

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
            return increaseQuantity(username, productId);


        return cartMapper.toCartProductDto(cartProduct);
    }

    @Transactional
    public CartProductDto increaseQuantity(String username, Long productId){
        CartProduct cartProduct = cartProductRepository.findByProductId(productId);
        cartProduct.setQuantity(cartProduct.getQuantity() + 1);

        Cart cart = cartRepository.findByUsername(username);
        cart.setTotalPrice(cart.getTotalPrice() + cartProduct.getPrice());

        cartRepository.save(cart);

        return cartMapper.toCartProductDto(cartProduct);
    }

    @Transactional
    public CartProductDto decreaseQuantity(String username, Long productId){

        CartProduct cartProduct = cartProductRepository.findByProductId(productId);
        cartProduct.setQuantity(cartProduct.getQuantity() - 1);

        Cart cart = cartRepository.findByUsername(username);
        cart.setTotalPrice(cart.getTotalPrice() - cartProduct.getPrice());

        cartRepository.save(cart);

        return cartMapper.toCartProductDto(cartProduct);
    }

    @Transactional
    public void removeProduct(String username, Long productId){
        CartProduct cartProduct = cartProductRepository.findByProductId(productId);
        Cart cart = cartRepository.findByUsername(username);

        cart.setTotalPrice(cart.getTotalPrice() - cartProduct.getPrice() * cartProduct.getQuantity());

        cart.getProducts().remove(cartProduct);
        cartProductRepository.delete(cartProduct);
        cartRepository.save(cart);
    }

    @Transactional
    public void clear(String username){
        Cart cart = cartRepository.findByUsername(username);
        cartProductRepository.removeAllByCartId(cart.getId());
        cartRepository.delete(cart);
        createCart(username);
    }

    @Transactional
    public OrderDto createOrder(String username){
        CartDto cart = getCartByUsername(username);

        if (cart != null && !cart.getProducts().isEmpty()){
            OrderDto orderDto = new OrderDto();

            orderDto.setUsername(username);
            orderDto.setOrderDate(LocalDate.now());
            orderDto.setStatus(Status.PAID);
            orderDto.setProducts(cart.getProducts()
                    .stream()
                    .map(cartMapper::toOrderProductDto)
                    .toList());
            orderDto.setTotalAmount(cart.getTotalPrice());

            clear(username);

            cartProducerManager.sendNewCartMessage(orderDto);

            return orderDto;
        }
        return null;
    }
}

//TODO exceptions