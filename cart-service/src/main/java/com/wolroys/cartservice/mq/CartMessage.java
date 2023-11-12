package com.wolroys.cartservice.mq;

import com.wolroys.shopentity.dto.OrderDto;
import com.wolroys.shopentity.dto.cart.CartDto;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.function.Supplier;

@Configuration
@Getter
public class CartMessage {

    private Sinks.Many<Message<OrderDto>> innerBus = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    @Bean
    public Supplier<Flux<Message<OrderDto>>> newOrderProduce(){
        return () -> innerBus.asFlux();
    }
}