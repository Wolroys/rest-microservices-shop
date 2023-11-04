package com.wolroys.shoputils.webClient;

import com.wolroys.shopentity.dto.ProductDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class ProductWebClientBuilder {

    private static final String GET_PRODUCT_URL = "http://localhost:8765/shop/";

    public Optional<ProductDto> getProductDto(Long productId){
            return WebClient
                    .create(GET_PRODUCT_URL + productId)
                    .get()
                    .retrieve()
                    .bodyToMono(ProductDto.class)
                    .blockOptional();
    }
}
