package com.wolroys.itemservice.repository;

import com.wolroys.shopentity.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String name);

}
