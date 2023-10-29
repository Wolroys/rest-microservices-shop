package com.wolroys.shopentity.mapper;

import com.wolroys.shopentity.dto.ProductCreateEditDto;
import com.wolroys.shopentity.dto.ProductDto;
import com.wolroys.shopentity.entity.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper modelMapper;

    public ProductDto mapToDto(Product product){
        return product == null ? null : modelMapper.map(product, ProductDto.class);
    }

    public Product mapToEntity(ProductCreateEditDto productDto){
        return productDto == null ? null : modelMapper.map(productDto, Product.class);
    }

    public <T> void map(T source, T destinationClass){
        modelMapper.map(source, destinationClass);
    }
}
