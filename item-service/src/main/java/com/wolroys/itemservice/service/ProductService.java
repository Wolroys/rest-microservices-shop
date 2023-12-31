package com.wolroys.itemservice.service;

import com.wolroys.itemservice.repository.CategoryRepository;
import com.wolroys.itemservice.repository.ProductRepository;
import com.wolroys.shopentity.dto.ProductCreateEditDto;
import com.wolroys.shopentity.dto.ProductDto;
import com.wolroys.shopentity.entity.Category;
import com.wolroys.shopentity.entity.Product;
import com.wolroys.shopentity.mapper.ProductMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public List<ProductDto> findAll(){
        return productRepository.findAll()
                .stream()
                .map(productMapper::mapToDto)
                .toList();
    }

    public Optional<ProductDto> findProduct(Long id){
        return productRepository.findById(id)
                .map(productMapper::mapToDto);
    }

    public List<ProductDto> findByCategory(String name){
        return productRepository.findByCategoryName(name)
                .stream()
                .map(productMapper::mapToDto)
                .toList();
    }


    @Transactional
    public ProductDto addProduct(ProductCreateEditDto productDto){
        Category category = categoryRepository.findByName(productDto.getCategoryTitle())
                .orElseThrow();

        return Optional.of(productDto)
                .map(productMapper::mapToEntity)
                .map(entity -> {
                    entity.setCategory(category);
                    return entity;
                })
                .map(productRepository::saveAndFlush)
                .map(productMapper::mapToDto)
                .orElseThrow();

    }

    @Transactional
    public Optional<ProductDto> updateProduct(Long id, ProductCreateEditDto productDto){
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            productMapper.map(productDto, product);

            return product
                    .map(productRepository::saveAndFlush)
                    .map(productMapper::mapToDto);
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

//    public List<ProductDto> findAllBySellerId(Long id){
//
//    }
}
