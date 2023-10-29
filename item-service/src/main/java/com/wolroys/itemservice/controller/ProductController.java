package com.wolroys.itemservice.controller;

import com.wolroys.itemservice.service.ProductService;
import com.wolroys.shopentity.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/shop")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> findAll(){
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product findProduct(@PathVariable Long id){
        return productService.findProduct(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/category")
    public List<Product> findProductByCategory(@RequestBody String category){
        return productService.findByCategory(category);
    }

    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @PatchMapping("/update/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product){
        return productService.updateProduct(id, product)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        if (!productService.remove(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
