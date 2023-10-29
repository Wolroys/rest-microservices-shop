package com.wolroys.itemservice.service;

import com.wolroys.itemservice.repository.ProductRepository;
import com.wolroys.shopentity.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public Optional<Product> findProduct(Long id){
        return productRepository.findById(id);
    }

    public List<Product> findByCategory(String name){
        return productRepository.findByCategoryName(name);
    }


    @Transactional
    public Product addProduct(Product product){
        return Optional.of(product)
                .map(productRepository::saveAndFlush)
                .orElseThrow();

    }

    @Transactional
    public Optional<Product> updateProduct(Long id, Product productDto){

        if (productRepository.findById(id).isPresent()){
            productRepository.saveAndFlush(productDto);

            return productRepository.findById(id);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean remove(Long id){
        return productRepository.findById(id)
                .map(entity -> {
                    productRepository.delete(entity);
                    productRepository.flush();
                    return true;
                }).orElse(false);

    }
}
