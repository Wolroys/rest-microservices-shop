package com.wolroys.itemservice.repository;

import com.wolroys.itemservice.config.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
@RequiredArgsConstructor
public class ProductRepositoryTest extends IntegrationTestBase {

    private final ProductRepository productRepository;

    @Test
    void checkRead(){
        var products = productRepository.findAll();
        assertThat(products).hasSize(2);
    }
}
