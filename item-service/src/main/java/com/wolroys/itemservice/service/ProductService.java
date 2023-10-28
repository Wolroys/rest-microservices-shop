package com.wolroys.itemservice.service;

import com.wolroys.itemservice.entity.Product;
import com.wolroys.itemservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public Optional<Product> findProduct(Long id){
        return productRepository.findById(id);
    }
}
