package com.wolroys.itemservice.controller;

import com.wolroys.itemservice.service.ProductService;
import com.wolroys.shopentity.dto.ProductCreateEditDto;
import com.wolroys.shopentity.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @
    public List<ProductDto> findAll(){
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductDto findProduct(@PathVariable Long id){
        return productService.findProduct(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/category")
    public List<ProductDto> findProductByCategory(@RequestBody String category){
        return productService.findByCategory(category);
    }

    @PostMapping("/add")
    public ProductDto addProduct(@RequestBody ProductCreateEditDto productDto){
        return productService.addProduct(productDto);
    }

    @PatchMapping("/update/{id}")
    public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductCreateEditDto product){
        return productService.updateProduct(id, product)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        if (!productService.remove(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
