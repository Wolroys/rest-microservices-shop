package com.wolroys.orderservice.mq;

import com.wolroys.orderservice.service.OrderService;
import com.wolroys.shopentity.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class MessageConsumer {

    private final OrderService orderService;

    @Bean
    public Consumer<Message<OrderDto>> newOrderConsumer(){
        return orderDtoMessage -> orderService.createOrder(orderDtoMessage.getPayload());
    }

}
