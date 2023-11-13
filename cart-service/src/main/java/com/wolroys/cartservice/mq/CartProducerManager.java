package com.wolroys.cartservice.mq;

import com.wolroys.shopentity.dto.OrderDto;
import lombok.Getter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
@Getter
public class CartProducerManager {

    private CartMessage cartMessage;

    public CartProducerManager(CartMessage cartMessage) {
        this.cartMessage = cartMessage;
    }

    public void sendNewCartMessage(OrderDto orderDto){
        cartMessage.getInnerBus().emitNext(MessageBuilder.withPayload(orderDto).build(),
                Sinks.EmitFailureHandler.FAIL_FAST);
        System.out.println("Order was sent" + cartMessage.getInnerBus().name());
    }
}
