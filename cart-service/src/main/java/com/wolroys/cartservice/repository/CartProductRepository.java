package com.wolroys.cartservice.repository;

import com.wolroys.shopentity.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    CartProduct findByProductId(Long productId);

    void removeAllByCartId(Long cartId);
}
