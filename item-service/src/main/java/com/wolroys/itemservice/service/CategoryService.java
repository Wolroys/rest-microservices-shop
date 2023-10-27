package com.wolroys.itemservice.service;

import com.wolroys.itemservice.entity.Category;
import com.wolroys.itemservice.entity.Product;
import com.wolroys.itemservice.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
}
