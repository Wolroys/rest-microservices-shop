package com.wolroys.shoputils.webClient;

import com.wolroys.shopentity.dto.ProductDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Component
public class ProductWebClientBuilder {

    private static final String GET_PRODUCT_URL = "http://localhost:8765/shop/";

    public Optional<ProductDto> getProductDto(Long productId){
        try {
            return WebClient
                    .create(GET_PRODUCT_URL + productId)
                    .get()
                    .retrieve()
                    .bodyToMono(ProductDto.class)
                    .blockOptional();
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }
}
